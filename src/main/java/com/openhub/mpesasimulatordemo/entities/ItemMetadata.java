package com.openhub.mpesasimulatordemo.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ItemMetadata {
    private String name;
    private String value;
}
