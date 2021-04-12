package client.fix.hnx.Dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class TopNPriceDto {
    private String symbol;
    private int noTopPrice;
    private List<NoTopPriceDto> listNotopPrices = new ArrayList<>();
  

 
    
}
