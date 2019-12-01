package com.trpo.dominion;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class CardInfo {
    private final String name;
    private final String description;
    private final String type;//тип действия, монетки, провинция
    private final Integer cost;
    private final Integer action;//кол-во дополнительных действий
    private final Integer buy;//кол-во дополнительных покупок
    private final Integer money;//кол-во дополнительных монеток
    private final Integer cards;//кол-во дополнительных карт
    private final Integer drop;//сколько карт сбросить
    private final Integer droptype;//все или только держатель карт скидывает через enum
    private final boolean protection;//защита от сброса через bool
    private final Integer trash;//сколько скинуть в свалку
    private final String imageId;
}
