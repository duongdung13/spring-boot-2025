package com.dungcode.demo.mongodb.model;

import com.dungcode.demo.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "posts")
public class Post {
    @Id
    private String id;

    private String title;

    private String content;

    private PostType postType;

    private Boolean isActive = true;

    @CreatedDate
    @Field()
    private Date createdAt;

    @LastModifiedDate
    @Field()
    private Date updatedAt;
}