package com.example.codepile.data.factories;

import com.example.codepile.data.entities.Pile;
import com.example.codepile.data.enums.AceMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

@Getter
@Setter
@NoArgsConstructor
public class PileFactory implements FactoryBean<Pile> {
    private int factoryId;
    private String pileId;
    private AceMode aceMode;
    private String title;
    private boolean readOnly;
    private String pileText;


    @Override
    public Pile getObject() throws Exception {
        return new Pile(pileId, aceMode, title, readOnly, pileText);
    }

    @Override
    public Class<?> getObjectType() {
        return Pile.class;
    }
}
