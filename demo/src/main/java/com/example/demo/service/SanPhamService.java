package com.example.demo.service;

import com.example.demo.model.SanPham;
import com.example.demo.repository.SanPhamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SanPhamService {

    private final SanPhamRepository sanPhamRepository;

    public Page<SanPham> getAll(Pageable pageable) {
        return sanPhamRepository.findAll(pageable);
    }

    public Page<SanPham> searchByName(String name, Pageable pageable) {
        return sanPhamRepository.findByNameContaining(name, pageable);
    }

    public Page<SanPham> searchByLoai(Long loaiId, Pageable pageable) {
        return sanPhamRepository.findByLoaiSanPhamId(loaiId, pageable);
    }

    public Page<SanPham> searchByMinPrice(BigDecimal price, Pageable pageable) {
        return sanPhamRepository.findByPriceGreaterThanEqual(price, pageable);
    }

    public SanPham getById(Long id) {
        return sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
    }

    @Transactional
    public SanPham create(SanPham sp){
        if(sp.getName().length()<5 || sp.getName().length()>50)
            throw new RuntimeException("Tên sản phẩm 5-50 ký tự");
        if(sp.getPrice().compareTo(new BigDecimal(100000)) < 0)
            throw new RuntimeException("Giá tối thiểu 100.000");
        if(sp.getLoaiSanPham() == null)
            throw new RuntimeException("Chọn loại sản phẩm");
        return sanPhamRepository.save(sp);
    }

    @Transactional
    public SanPham update(Long id, SanPham sp){
        SanPham old = getById(id);
        old.setName(sp.getName());
        old.setPrice(sp.getPrice());
        old.setStatus(sp.getStatus());
        old.setLoaiSanPham(sp.getLoaiSanPham());
        return sanPhamRepository.save(old);
    }

    @Transactional
    public void deleteByIds(java.util.List<Long> ids){
        sanPhamRepository.deleteAllById(ids);
    }
}
