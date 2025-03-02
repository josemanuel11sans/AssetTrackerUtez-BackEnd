package com.integradora.AssetTrackerUtez.Cloudinary.model;



import com.integradora.AssetTrackerUtez.Cloudinary.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {
}