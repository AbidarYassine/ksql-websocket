package ma.octo.demoksqlwebsocket.vo;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserVo {
    private Integer id;
    private String name;

}
