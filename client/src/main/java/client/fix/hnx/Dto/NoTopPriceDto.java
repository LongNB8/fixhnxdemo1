package client.fix.hnx.Dto;

import lombok.AllArgsConstructor;

import lombok.Getter;

import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor

public class NoTopPriceDto {

    private int numTopPrice;
    private String bestBidPrice;
    private String bestBidQtty;
    private String bestOfferPrice;
    private String bestOfferQtty;

    public NoTopPriceDto() {
        this.numTopPrice = 0;
        this.bestBidPrice = "";
        this.bestBidQtty = "";
        this.bestOfferPrice = "";
        this.bestOfferQtty = "";
    }

 


    
}
