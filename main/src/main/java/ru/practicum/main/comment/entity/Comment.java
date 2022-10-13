package ru.practicum.main.comment.entity;

import lombok.*;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "edited")
    private LocalDateTime edited;

    @Column(name = "text")
    private String text;

}
