package com.example.demo.service;

import com.example.demo.model.LoaiSanPham;
import com.example.demo.repository.LoaiSanPhamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoaiSanPhamService {

    private final LoaiSanPhamRepository repository;

    public List<LoaiSanPham> getAll(){
        return repository.findAll();
    }

    public LoaiSanPham getById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại sản phẩm"));
    }

    @Transactional
    public LoaiSanPham create(LoaiSanPham loai){
        if(loai.getName() == null || loai.getName().isBlank())
            throw new RuntimeException("Tên loại sản phẩm không được để trống");
        return repository.save(loai);
    }

    @Transactional
    public LoaiSanPham update(Long id, LoaiSanPham loai){
        LoaiSanPham old = getById(id);
        old.setName(loai.getName());
        return repository.save(old);
    }

    @Transactional
    public void delete(Long id){
        repository.deleteById(id);
    }
}
