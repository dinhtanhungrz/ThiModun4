const API_URL = "http://localhost:8080/api";
let currentPage = 0; // Lưu trang hiện tại để khi xóa/sửa xong còn biết load lại trang nào

// --- 1. LOAD SẢN PHẨM (Tìm kiếm + Phân trang) ---
async function loadProducts(page = 0) {
    currentPage = page;

    // Lấy giá trị từ các ô tìm kiếm
    const name = document.getElementById("searchName").value.trim();
    const price = document.getElementById("searchPrice").value;
    const catId = document.getElementById("searchCategory").value;

    // Tạo URL query param (Khớp với Controller mới)
    // Mặc định lấy trang 'page', mỗi trang 5 phần tử
    let url = `${API_URL}/san-pham?page=${page}&size=5`;

    // Nếu người dùng có nhập gì thì nối thêm vào URL
    if (name) url += `&name=${encodeURIComponent(name)}`;
    if (price) url += `&price=${price}`;
    if (catId) url += `&categoryId=${catId}`;

    try {
        const res = await fetch(url);
        const data = await res.json(); 
        // Backend trả về: { content: [...], totalPages: 10, ... }

        renderTable(data.content, page);
        renderPagination(data.totalPages, page);
    } catch (err) {
        console.error("Lỗi tải danh sách:", err);
    }
}

// --- 2. Render Bảng ---
function renderTable(products, page) {
    const tbody = document.getElementById("tableBody");
    tbody.innerHTML = "";

    if (products.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" class="text-center">Không tìm thấy kết quả nào</td></tr>`;
        return;
    }

    products.forEach((sp, index) => {
        // Tính số thứ tự tăng dần theo trang
        const stt = (page * 5) + index + 1;
        
        // Format tiền Việt Nam
        const priceVND = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(sp.price);
        
        // Xử lý hiển thị tên loại (tránh lỗi nếu null)
        const tenLoai = sp.loaiSanPham ? sp.loaiSanPham.name : 'Chưa phân loại';

        const row = `
            <tr>
                <td class="text-center">${stt}</td>
                <td class="text-center">
                    <input type="checkbox" class="product-check" value="${sp.id}">
                </td>
                <td class="fw-bold">${sp.name}</td>
                <td>${priceVND}</td>
                <td>${tenLoai}</td>
                <td>${sp.status || ''}</td>
                <td class="text-center">
                    <button class="btn btn-sm btn-warning" onclick="alert('Chức năng sửa đang phát triển')">Sửa</button>
                </td>
            </tr>
        `;
        tbody.innerHTML += row;
    });
}

// --- 3. Render Phân Trang ---
function renderPagination(totalPages, page) {
    const ul = document.getElementById("pagination");
    ul.innerHTML = "";

    if (totalPages <= 1) return; // 1 trang thì khỏi hiện

    // Nút Trước
    ul.innerHTML += `
        <li class="page-item ${page === 0 ? 'disabled' : ''}">
            <button class="page-link" onclick="loadProducts(${page - 1})">Trước</button>
        </li>
    `;

    // Số trang hiện tại
    ul.innerHTML += `
        <li class="page-item active">
            <span class="page-link">${page + 1} / ${totalPages}</span>
        </li>
    `;

    // Nút Sau
    ul.innerHTML += `
        <li class="page-item ${page === totalPages - 1 ? 'disabled' : ''}">
            <button class="page-link" onclick="loadProducts(${page + 1})">Sau</button>
        </li>
    `;
}

// --- 4. Load Dropdown Loại Sản Phẩm ---
async function loadCategories(selectId) {
    try {
        const res = await fetch(`${API_URL}/loai-san-pham`);
        const list = await res.json();
        
        const select = document.getElementById(selectId);
        // Giữ lại option đầu tiên
        select.innerHTML = select.firstElementChild.outerHTML;

        list.forEach(item => {
            select.innerHTML += `<option value="${item.id}">${item.name}</option>`;
        });
    } catch (err) {
        console.error("Lỗi load loại:", err);
    }
}

// --- 5. Xóa Nhiều Sản Phẩm ---
async function deleteMultiple() {
    // Lấy các ô đã tick
    const checked = document.querySelectorAll(".product-check:checked");
    const ids = Array.from(checked).map(cb => parseInt(cb.value));

    if (ids.length === 0) {
        alert("Vui lòng chọn sản phẩm để xóa!");
        return;
    }

    if (!confirm("Bạn có chắc muốn xóa các sản phẩm đã chọn?")) return;

    try {
        const res = await fetch(`${API_URL}/san-pham`, {
            method: "DELETE",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(ids) // Gửi mảng ID lên: [1, 2, 5]
        });

        if (res.ok) {
            alert("Đã xóa thành công!");
            loadProducts(currentPage); // Load lại trang hiện tại
            document.getElementById("checkAll").checked = false;
        } else {
            alert("Lỗi khi xóa!");
        }
    } catch (err) {
        console.error(err);
    }
}

// --- 6. Thêm Sản Phẩm (Có Validate) ---
async function handleAddProduct(e) {
    e.preventDefault();

    // Lấy dữ liệu
    const name = document.getElementById("name").value.trim();
    const price = parseFloat(document.getElementById("price").value);
    const catId = document.getElementById("categorySelect").value;
    const status = document.getElementById("status").value;

    // Reset lỗi
    document.getElementById("errName").innerText = "";
    document.getElementById("errPrice").innerText = "";

    // Validate
    let valid = true;
    if (name.length < 5 || name.length > 50) {
        document.getElementById("errName").innerText = "Tên phải từ 5-50 ký tự";
        valid = false;
    }
    if (isNaN(price) || price < 100000) {
        document.getElementById("errPrice").innerText = "Giá phải >= 100.000";
        valid = false;
    }
    if (!catId) {
        alert("Chưa chọn loại sản phẩm!");
        valid = false;
    }

    if (!valid) return;

    // Gọi API
    const payload = {
        name: name,
        price: price,
        status: status,
        loaiSanPham: { id: catId }
    };

    try {
        const res = await fetch(`${API_URL}/san-pham`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            alert("Thêm thành công!");
            window.location.href = "index.html";
        } else {
            alert("Lỗi server!");
        }
    } catch (err) {
        console.error(err);
    }
}

// --- 7. Check All ---
function toggleCheckAll() {
    const status = document.getElementById("checkAll").checked;
    document.querySelectorAll(".product-check").forEach(cb => cb.checked = status);
}

// --- 8. Reset Tìm Kiếm ---
function resetSearch() {
    document.getElementById("searchName").value = "";
    document.getElementById("searchPrice").value = "";
    document.getElementById("searchCategory").value = "";
    loadProducts(0);
}