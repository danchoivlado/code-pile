package com.example.codepile.services.services;

import com.example.codepile.data.converters.AceConverter;
import com.example.codepile.data.entities.Pile;
import com.example.codepile.data.entities.User;
import com.example.codepile.data.enums.AceMode;
import com.example.codepile.data.factories.PileFactory;
import com.example.codepile.data.models.service.pile.*;
import com.example.codepile.data.models.view.piles.PileViewModel;
import com.example.codepile.data.repositories.PileRepository;
import com.example.codepile.data.repositories.UserRepository;
import com.example.codepile.error.pile.PileNotFoundException;
import com.example.codepile.error.user.UserNotFoundException;
import com.example.codepile.services.AlphanumericString;
import com.example.codepile.services.PileService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.codepile.data.validation.MessageCodes.PILE_ID_NOTFOUND;
import static com.example.codepile.data.validation.MessageCodes.USER_USERNAME_NOTFOUND;

@Service
public class PileServiceImpl implements PileService {
    private PileRepository pileRepository;
    private Pile generatedPile;
    private ModelMapper modelMapper;
    private UserRepository userRepository;
    private AlphanumericString alphanumericString;

    public PileServiceImpl(PileRepository pileRepository,
                           Pile generatedPile,
                           ModelMapper modelMapper,
                           UserRepository userRepository,
                           AlphanumericString alphanumericString) {
        this.pileRepository = pileRepository;
        this.generatedPile = generatedPile;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.alphanumericString = alphanumericString;
    }

    @Override
    public PileServiceModel getPileWithId(String id) {
        this.checkIfPileExistsWithId(id);

        Pile pile = this.pileRepository.findPileById(id);
        return modelMapper.map(pile, PileServiceModel.class);
    }

    @Override
    public PileCreateServiceModel createPile(String byUserWithUsername) {
        this.checkIfUserExistsWithUserName(byUserWithUsername);

        User createdBy = this.userRepository.findUserByUsername(byUserWithUsername);
        generatedPile.setUser(createdBy);
        String id = alphanumericString.getRandomAlphanumericString();
        generatedPile.setId(id);
        generatedPile.setTitle(id);
        this.pileRepository.save(generatedPile);
        return new PileCreateServiceModel(generatedPile.getId());
    }

    @Override
    public MyPilesServiceViewModel getMyPiles(String byUserWithUsername) {
        this.checkIfUserExistsWithUserName(byUserWithUsername);

        List<Pile> piles = this.pileRepository.findAllByUserUsername(byUserWithUsername);
        List<MyPileServiceViewModel> myPileServiceViewModels = piles.stream()
                .map(pile -> modelMapper.map(pile, MyPileServiceViewModel.class)).collect(Collectors.toList());
        return new MyPilesServiceViewModel(myPileServiceViewModels);
    }

    @Override
    public void deletePileWithId(String pileId) {
        this.checkIfPileExistsWithId(pileId);
        Pile pile = this.pileRepository.getById(pileId);
        this.pileRepository.delete(pile);
    }

    @Override
    public void changeTitle(String pileId, String title) {
        this.checkIfPileExistsWithId(pileId);
        Pile pile = this.pileRepository.findPileById(pileId);
        pile.setTitle(title);
        this.pileRepository.save(pile);
    }

    @Override
    public void changeLanguage(String pileId, String language) {
        this.checkIfPileExistsWithId(pileId);
        Pile pile = this.pileRepository.findPileById(pileId);
        AceConverter aceConverter = new AceConverter();
        pile.setAceMode(aceConverter.convertToEntityAttribute(language));
        this.pileRepository.save(pile);
    }

    @Override
    public void changeEditorText(String pileId, String editorText) {
        this.checkIfPileExistsWithId(pileId);
        Pile pile = this.pileRepository.findPileById(pileId);
        pile.setPileText(editorText);
        this.pileRepository.save(pile);
    }

    @Override
    public boolean isCurrentUserOwner(Principal principal, String pileUserId) {
        if (principal == null) {
            return false;
        }
        String currentUserId = this.userRepository.findUserByUsername(principal.getName()).getId();

        if (currentUserId.equals(pileUserId))
            return true;

        return false;
    }

    @Override
    public boolean canCurrentUserEdit(Principal principal, String pileId) {
        this.checkIfPileExistsWithId(pileId);
        Pile pile = this.pileRepository.findPileById(pileId);
        if (pile.isReadOnly() == false) {
            return true;
        } else {
            User user = this.userRepository.findUserByUsername(principal.getName());
            if (pile.getUser().getId().equals(user.getId()))
                return true;
        }
        return false;
    }

    @Override
    public boolean canCurrentUserChangeMode(Principal principal, String pileId) {
        this.checkIfPileExistsWithId(pileId);
        Pile pile = this.pileRepository.findPileById(pileId);
        if (principal == null) return false;
        User user = this.userRepository.findUserByUsername(principal.getName());
        if (pile.getUser().getId().equals(user.getId()))
            return true;
        return false;
    }

    @Override
    public ChangeAccessModeServiceModel changeAccessMode(boolean readOnly, String pileId, Principal principal) {
        Pile pile = this.pileRepository.findPileById(pileId);
        pile.setReadOnly(readOnly);
        this.pileRepository.save(pile);
        ChangeAccessModeServiceModel serviceModel = new ChangeAccessModeServiceModel();
        User user = this.userRepository.findUserByUsername(principal.getName());
        serviceModel.setOwner(user.getId().equals(pile.getUser().getId())? true : false);
        serviceModel.setSubscription(readOnly == true ? pile.getUser().getId() : pileId);
        serviceModel.setReadOnly(readOnly);


        return serviceModel;
    }

    private void checkIfUserExistsWithUserName(String username) {
        boolean existsUserWithUsrername = this.userRepository.existsUserByUsername(username);
        if (!existsUserWithUsrername) {
            throw new UsernameNotFoundException(USER_USERNAME_NOTFOUND);
        }
    }

    private void checkIfPileExistsWithId(String pileId) {
        boolean existsPileWithId = this.pileRepository.existsPileById(pileId);
        if (!existsPileWithId) {
            throw new PileNotFoundException(PILE_ID_NOTFOUND);
        }
    }
}
