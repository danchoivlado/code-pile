package com.example.codepile.services.services;

import com.example.codepile.data.entities.Pile;
import com.example.codepile.data.factories.PileFactory;
import com.example.codepile.data.models.service.pile.PileServiceModel;
import com.example.codepile.data.repositories.PileRepository;
import com.example.codepile.services.PileService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PileServiceImpl implements PileService {
    private PileRepository pileRepository;
    private Pile generatedPile;
    private ModelMapper modelMapper;

    public PileServiceImpl(PileRepository pileRepository, Pile generatedPile, ModelMapper modelMapper) {
        this.pileRepository = pileRepository;
        this.generatedPile = generatedPile;
        this.modelMapper = modelMapper;
    }

    @Override
    public PileServiceModel getPileWithId(String id) {
        if (!pileRepository.existsPileById(id)) {
            this.pileRepository.save(generatedPile);
            return modelMapper.map(generatedPile, PileServiceModel.class);
        }

        Pile pile = this.pileRepository.findPileById(id);
        return modelMapper.map(pile, PileServiceModel.class);
    }
}
