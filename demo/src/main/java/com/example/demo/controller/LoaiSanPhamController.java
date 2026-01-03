package com.example.demo.controller;

import com.example.demo.model.LoaiSanPham;
import com.example.demo.service.LoaiSanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/loai-san-pham")
@RequiredArgsConstructor
public class LoaiSanPhamController {

    private final LoaiSanPhamService service;

    // 1. Lấy tất cả loại sản phẩm
    @GetMapping
    public ResponseEntity<List<LoaiSanPham>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    // 2. Lấy theo ID
    @GetMapping("/{id}")
    public ResponseEntity<LoaiSanPham> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getById(id));
    }

    // 3. Thêm loại sản phẩm mới
    @PostMapping
    public ResponseEntity<LoaiSanPham> create(@RequestBody LoaiSanPham loai){
        return ResponseEntity.ok(service.create(loai));
    }

    // 4. Cập nhật loại sản phẩm
    @PutMapping("/{id}")
    public ResponseEntity<LoaiSanPham> update(@PathVariable Long id, @RequestBody LoaiSanPham loai){
        return ResponseEntity.ok(service.update(id, loai));
    }

    // 5. Xóa loại sản phẩm
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
