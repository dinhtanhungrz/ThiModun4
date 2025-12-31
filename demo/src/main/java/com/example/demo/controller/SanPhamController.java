package com.example.demo.controller;

import com.example.demo.model.SanPham;
import com.example.demo.service.SanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/san-pham")
@RequiredArgsConstructor
public class SanPhamController {

    private final SanPhamService service;

    @GetMapping
    public ResponseEntity<Page<SanPham>> getAll(Pageable pageable){
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping("/search-name")
    public ResponseEntity<Page<SanPham>> searchName(@RequestParam String name, Pageable pageable){
        return ResponseEntity.ok(service.searchByName(name, pageable));
    }

    @GetMapping("/search-loai")
    public ResponseEntity<Page<SanPham>> searchLoai(@RequestParam Long loaiId, Pageable pageable){
        return ResponseEntity.ok(service.searchByLoai(loaiId, pageable));
    }

    @GetMapping("/search-price")
    public ResponseEntity<Page<SanPham>> searchPrice(@RequestParam BigDecimal min, Pageable pageable){
        return ResponseEntity.ok(service.searchByMinPrice(min, pageable));
    }

    @PostMapping
    public ResponseEntity<SanPham> create(@RequestBody SanPham sp){
        return ResponseEntity.ok(service.create(sp));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanPham> update(@PathVariable Long id, @RequestBody SanPham sp){
        return ResponseEntity.ok(service.update(id, sp));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody List<Long> ids){
        service.deleteByIds(ids);
        return ResponseEntity.noContent().build();
    }
}
