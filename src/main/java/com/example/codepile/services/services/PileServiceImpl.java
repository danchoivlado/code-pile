package com.example.codepile.services.services;

import com.example.codepile.data.entities.Pile;
import com.example.codepile.data.entities.User;
import com.example.codepile.data.factories.PileFactory;
import com.example.codepile.data.models.service.pile.PileCreateServiceModel;
import com.example.codepile.data.models.service.pile.PileServiceModel;
import com.example.codepile.data.repositories.PileRepository;
import com.example.codepile.data.repositories.UserRepository;
import com.example.codepile.error.pile.PileNotFoundException;
import com.example.codepile.services.PileService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PileServiceImpl implements PileService {
    private PileRepository pileRepository;
    private Pile generatedPile;
    private ModelMapper modelMapper;
    private UserRepository userRepository;

    public PileServiceImpl(PileRepository pileRepository,
                           Pile generatedPile,
                           ModelMapper modelMapper,
                           UserRepository userRepository) {
        this.pileRepository = pileRepository;
        this.generatedPile = generatedPile;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public PileServiceModel getPileWithId(String id) {
        if (!pileRepository.existsPileById(id)) {
//            this.pileRepository.save(generatedPile);
//            return modelMapper.map(generatedPile, PileServiceModel.class);
            throw new PileNotFoundException("There is no pile");
        }

        Pile pile = this.pileRepository.findPileById(id);
        return modelMapper.map(pile, PileServiceModel.class);
    }

    @Override
    public PileCreateServiceModel createPile(String byUserWithUsername) {
        User createdBy = this.userRepository.findUserByUsername(byUserWithUsername);
        generatedPile.setUser(createdBy);
        this.pileRepository.save(generatedPile);
        return new PileCreateServiceModel(generatedPile.getId());
    }
}
