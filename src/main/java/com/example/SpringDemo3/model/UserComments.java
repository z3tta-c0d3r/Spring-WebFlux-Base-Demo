package com.example.SpringDemo3.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class UserComments {

    private User user;
    private Comments comments;

    @Override
    public String toString() {
        return "UserComments [user=" + user + ", comments=" + comments;
    }
}
