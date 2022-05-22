package ma.octo.demoksqlwebsocket.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CampaignStatusVo {
  private String type;
  private Integer total_campaign;
}
