package com.example.contactApp.Dto;
 import com.example.contactApp.model.Event;
        import com.fasterxml.jackson.annotation.JsonInclude;
        import lombok.AllArgsConstructor;
        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;

        import java.io.Serializable;
        import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDto implements Serializable {
    private Long id;
    private String title;
    private  String description;
    private LocalDateTime date;
    private GroupDto group;
    private UserDto user;


    public EventDto(Event event, boolean details) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.date = event.getDate();
        this.group = event.getGroup() != null ?
                new GroupDto(event.getGroup(), false) :
                null;
        this.user = event.getUser() != null ?
                new UserDto(event.getUser(), false) :
                null;

    }
}