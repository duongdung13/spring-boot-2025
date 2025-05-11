package com.dungcode.demo.posgresql.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "messages")
public class Message extends AbstractEntity implements Serializable {
    private String content;
    private Date timestamp;
} 