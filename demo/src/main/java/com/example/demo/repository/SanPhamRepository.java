package com.example.demo.repository;

import com.example.demo.model.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Long> {

    Page<SanPham> findByNameContaining(String name, Pageable pageable);

    Page<SanPham> findByLoaiSanPhamId(Long loaiId, Pageable pageable);

    Page<SanPham> findByPriceGreaterThanEqual(BigDecimal price, Pageable pageable);
}
