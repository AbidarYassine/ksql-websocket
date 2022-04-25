package ma.octo.demoksqlwebsocket.vo;

import lombok.*;

import java.math.BigInteger;

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
