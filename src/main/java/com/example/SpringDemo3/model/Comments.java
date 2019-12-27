package com.example.SpringDemo3.model;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Comments {
    private List<String> comments = new ArrayList<>();

    public void addComentario(String comentario){
        this.comments.add(comentario);
    }

    @Override
    public String toString() {
        return "comentarios=" + comments;
    }
}
