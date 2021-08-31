package com.example.codepile.services.services;

import com.example.codepile.base.TestBase;
import com.example.codepile.data.entities.Pile;
import com.example.codepile.data.entities.User;
import com.example.codepile.data.enums.AceMode;
import com.example.codepile.data.models.service.pile.MyPileServiceViewModel;
import com.example.codepile.data.models.service.pile.MyPilesServiceViewModel;
import com.example.codepile.data.models.service.pile.PileCreateServiceModel;
import com.example.codepile.data.models.service.pile.PileServiceModel;
import com.example.codepile.data.repositories.PileRepository;
import com.example.codepile.data.repositories.UserRepository;
import com.example.codepile.error.ace.AceNotFoundException;
import com.example.codepile.error.pile.PileNotFoundException;
import com.example.codepile.services.AlphanumericString;
import com.example.codepile.services.PileService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.objenesis.instantiator.basic.ObjectInputStreamInstantiator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PileServiceImplTest extends TestBase {
    private final String sampleSearchedPileId = "1";
    private final String samplePileTitle = "title";
    private final String samplePileText = "text";
    private final boolean samplePileReadOnly = true;
    private final AceMode samplePileAceMode = AceMode.HTML;


    @Autowired
    PileService pileService;

    @MockBean
    PileRepository pileRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    AlphanumericString alphanumericString;

    @Autowired
    Pile generatedPile;

    @MockBean
    Principal principal;


    @Test
    void getPileWithId_WhenPileWithThisIdDoesNOTExists_ShouldThrowPileNotFoundException() {
        String searchedPileWithId = "-1";

        when(this.pileRepository.existsPileById(searchedPileWithId))
                .thenReturn(false);

        assertThrows(PileNotFoundException.class, () -> this.pileService.getPileWithId(searchedPileWithId));
    }

    @Test
    void getPileWithId_WhenPileWithThisIdDoesExists_ShouldGetProperlyMappedPileServiceModel() {
        User dbUser = new User();
        dbUser.setUsername("user");
        Pile dbPile = new Pile();
        dbPile.setUser(dbUser);
        dbPile.setId(sampleSearchedPileId);
        dbPile.setTitle(samplePileTitle);
        dbPile.setAceMode(samplePileAceMode);
        dbPile.setReadOnly(samplePileReadOnly);
        dbPile.setPileText(samplePileText);

        when(this.pileRepository.existsPileById(sampleSearchedPileId))
                .thenReturn(true);
        when(this.pileRepository.findPileById(sampleSearchedPileId))
                .thenReturn(dbPile);

        PileServiceModel serviceModel = this.pileService.getPileWithId(sampleSearchedPileId);

        assertEquals(serviceModel.getUser().getUsername(), dbPile.getUser().getUsername());
        assertEquals(serviceModel.getId(), dbPile.getId());
        assertEquals(serviceModel.getTitle(), dbPile.getTitle());
        assertEquals(serviceModel.getAceMode().getId(), dbPile.getAceMode().getId());
        assertEquals(serviceModel.isReadOnly(), dbPile.isReadOnly());
        assertEquals(serviceModel.getPileText(), dbPile.getPileText());
    }

    //PileNotFoundException
    @Test
    void createPile_WhenUserDoesExistsWithGiveId_ShouldSaveProperCreatedPile() throws Exception {
        String searchedUserUserName = "1";
        String sampleAlphanumericString = "12";
        Pile customGeneratedPile = new Pile();
        customGeneratedPile.setPileText(samplePileText);
        customGeneratedPile.setReadOnly(samplePileReadOnly);
        customGeneratedPile.setAceMode(samplePileAceMode);
        User dbUser = new User();
        dbUser.setUsername("user");

        when(this.userRepository.existsUserByUsername(searchedUserUserName))
                .thenReturn(true);
        when(this.alphanumericString.getRandomAlphanumericString())
                .thenReturn(sampleAlphanumericString);
        when(this.userRepository.findUserByUsername(searchedUserUserName))
                .thenReturn(dbUser);


        PileCreateServiceModel serviceModel = this.pileService.createPile(searchedUserUserName);

        ArgumentCaptor<Pile> argument = ArgumentCaptor.forClass(Pile.class);
        verify(pileRepository).save(argument.capture());

        Pile savedPile = argument.getValue();

        assertEquals(serviceModel.getPileId(), sampleAlphanumericString);
        assertEquals(savedPile.getId(), sampleAlphanumericString);
        assertEquals(savedPile.getTitle(), sampleAlphanumericString);
    }


    @Test
    void createPile_WhenUserDoesNOTExistsWithGiveId_ShouldThrowUsernameNotFoundException() {
        String searchedUserId = "-1";

        when(this.userRepository.existsUserByUsername(searchedUserId))
                .thenReturn(false);

        assertThrows(UsernameNotFoundException.class, () -> this.pileService.createPile(searchedUserId));
    }

    @Test
    void getMyPiles_WhenByUserWithUserNameDoesNOTExists_ShouldThrowUsernameNotFoundException() {
        String searchedUserWithUserName = "user";

        when(this.userRepository.existsUserByUsername(searchedUserWithUserName))
                .thenReturn(false);

        assertThrows(UsernameNotFoundException.class, () -> this.pileService.getMyPiles(searchedUserWithUserName));
    }

    @Test
    void getMyPiles_WhenUserHaveZeroPiles_ShouldReturnZeroSizeList() {
        String searchedUserWithUserName = "user";

        when(this.userRepository.existsUserByUsername(searchedUserWithUserName))
                .thenReturn(true);
        when(this.pileRepository.findAllByUserUsername(searchedUserWithUserName))
                .thenReturn(new ArrayList<Pile>());

        MyPilesServiceViewModel myPilesServiceViewModel =
                this.pileService.getMyPiles(searchedUserWithUserName);

        assertEquals(myPilesServiceViewModel.getPiles().size(), 0);
    }

    @Test
    void getMyPiles_WhenUserHaveOnePile_ShouldReturnProperlyMappedList() {
        String searchedUserWithUserName = "user";
        Pile dbPile = new Pile();
        dbPile.setId(sampleSearchedPileId);
        dbPile.setTitle(samplePileTitle);
        dbPile.setAceMode(samplePileAceMode);
        List<Pile> pileList = List.of(dbPile);

        when(this.userRepository.existsUserByUsername(searchedUserWithUserName))
                .thenReturn(true);
        when(this.pileRepository.findAllByUserUsername(searchedUserWithUserName))
                .thenReturn(pileList);

        MyPilesServiceViewModel myPilesServiceViewModel =
                this.pileService.getMyPiles(searchedUserWithUserName);

        MyPileServiceViewModel returnedPile = myPilesServiceViewModel.getPiles().stream().findFirst().orElse(null);
        assertEquals(myPilesServiceViewModel.getPiles().size(), 1);
        assertEquals(returnedPile.getTitle(), dbPile.getTitle());
        assertEquals(returnedPile.getId(), dbPile.getId());
        assertEquals(returnedPile.getAceMode(), dbPile.getAceMode());
    }

    @Test
    void deletePileWithId_WhenPileDoesNOTExists_ShouldThrowPileNotFoundException() {
        String searchedPileId = "-1";

        when(this.pileRepository.existsPileById(searchedPileId))
                .thenReturn(false);

        assertThrows(PileNotFoundException.class, () -> this.pileService.deletePileWithId(searchedPileId));
    }

    @Test
    void deletePileWithId_WhenPileDoesExists_ShouldDeletePile() {
        String searchedPileId = "1";
        Pile dbPile = new Pile();
        dbPile.setId(sampleSearchedPileId);

        when(this.pileRepository.existsPileById(searchedPileId))
                .thenReturn(true);
        when(this.pileRepository.getById(searchedPileId))
                .thenReturn(dbPile);

        this.pileService.deletePileWithId(searchedPileId);

        ArgumentCaptor<Pile> argument = ArgumentCaptor.forClass(Pile.class);
        verify(pileRepository).delete(argument.capture());

        Pile deletedPile = argument.getValue();

        assertEquals(dbPile.getId(), deletedPile.getId());
    }

    @Test
    void changeTitle_WhenPileIdDoesNotExists_ShouldThrowPileNotFoundException() {
        String searchedPileId = "-1";

        when(this.pileRepository.existsPileById(searchedPileId))
                .thenReturn(false);

        assertThrows(PileNotFoundException.class, () -> this.pileService.changeTitle(searchedPileId, samplePileTitle));
    }

    @Test
    void changeTitle_WhenPileIdDoesExists_ShouldChangeTitleProperly() {
        String searchedPileId = "1";
        String inputNewTitle = "new";
        String oldTitle = "old";

        Pile dbPile = new Pile();
        dbPile.setId(searchedPileId);
        dbPile.setTitle(oldTitle);

        when(this.pileRepository.existsPileById(searchedPileId))
                .thenReturn(true);
        when(this.pileRepository.findPileById(searchedPileId))
                .thenReturn(dbPile);

        this.pileService.changeTitle(searchedPileId, inputNewTitle);

        ArgumentCaptor<Pile> argument = ArgumentCaptor.forClass(Pile.class);
        verify(pileRepository).save(argument.capture());

        Pile updatedPile = argument.getValue();

        assertEquals(updatedPile.getTitle(), inputNewTitle);
    }

    @Test
    void changeLanguage_WhenPileWithIdDoesNOTExists_ShouldThrowPileNotFoundException() {

        String searchedPileId = "-1";

        when(this.pileRepository.existsPileById(searchedPileId))
                .thenReturn(false);

        assertThrows(PileNotFoundException.class,
                () -> this.pileService.changeLanguage(searchedPileId, samplePileAceMode.getId()));
    }

    @Test
    void changeLanguage_WhenLanguageDoesNOTExists_ShouldThrowAceNotFoundException() {

        String searchedPileId = "1";
        String inputNewLanguage = "non-existent";
        AceMode oldLanguage = AceMode.HTML;

        Pile dbPile = new Pile();
        dbPile.setId(searchedPileId);
        dbPile.setAceMode(oldLanguage);

        when(this.pileRepository.existsPileById(searchedPileId))
                .thenReturn(true);
        when(this.pileRepository.findPileById(searchedPileId))
                .thenReturn(dbPile);

        assertThrows(AceNotFoundException.class, () -> this.pileService.changeLanguage(searchedPileId, inputNewLanguage));

    }

    @Test
    void changeLanguage_WhenLanguageDoesExists_ShouldChangeLanguageProperly() {

        String searchedPileId = "1";
        String inputNewLanguage = AceMode.JAVASCRIPT.getId();
        AceMode oldLanguage = AceMode.HTML;

        Pile dbPile = new Pile();
        dbPile.setId(searchedPileId);
        dbPile.setAceMode(oldLanguage);

        when(this.pileRepository.existsPileById(searchedPileId))
                .thenReturn(true);
        when(this.pileRepository.findPileById(searchedPileId))
                .thenReturn(dbPile);

        this.pileService.changeLanguage(searchedPileId, inputNewLanguage);

        ArgumentCaptor<Pile> argument = ArgumentCaptor.forClass(Pile.class);
        verify(pileRepository).save(argument.capture());

        Pile updatedPile = argument.getValue();

        assertEquals(updatedPile.getAceMode().getId(), inputNewLanguage);
    }


    @Test
    void changeEditorText_WhenPileWithIdDoesNOTExists_ShouldThrowPileNotFoundException() {
        String searchedPileId = "-1";

        when(this.pileRepository.existsPileById(searchedPileId))
                .thenReturn(false);

        assertThrows(PileNotFoundException.class,
                () -> this.pileService.changeEditorText(searchedPileId, samplePileText));
    }

    @Test
    void changeEditorText_WhenPileWithIdDoesExists_ShouldChangePileText() {
        String searchedPileId = "1";
        String inputNewPileText = "new";
        String oldPileText = "old";

        Pile dbPile = new Pile();
        dbPile.setId(searchedPileId);
        dbPile.setPileText(oldPileText);

        when(this.pileRepository.existsPileById(searchedPileId))
                .thenReturn(true);
        when(this.pileRepository.findPileById(searchedPileId))
                .thenReturn(dbPile);

        this.pileService.changeEditorText(searchedPileId, inputNewPileText);

        ArgumentCaptor<Pile> argument = ArgumentCaptor.forClass(Pile.class);
        verify(pileRepository).save(argument.capture());

        Pile updatedPile = argument.getValue();

        assertEquals(updatedPile.getPileText(), inputNewPileText);
        ;
    }

    @Test
    void isCurrentUserOwner_WhenPrincipalIsNull_ShouldReturnFalse() {
        String searchedPileId = "1";
        assertFalse(this.pileService.isCurrentUserOwner(null, searchedPileId));
    }

    @Test
    void isCurrentUserOwner_WhenAccessedWithOtherUser_ShouldReturnFalse() {
        String searchedPileUserId = "non-this-user";
        String userId = "userId";
        String userName = "user";
        User dbUser = new User();
        dbUser.setId(userId);
        dbUser.setUsername(userName);

        when(principal.getName())
                .thenReturn(userName);
        when(this.userRepository.findUserByUsername(userName))
                .thenReturn(dbUser);

        assertFalse(this.pileService.isCurrentUserOwner(principal, searchedPileUserId));
    }

    @Test
    void isCurrentUserOwner_WhenAccessedWithSameUser_ShouldReturnTrue() {
        String searchedPileUserId = "userId";
        String userId = "userId";
        String userName = "user";
        User dbUser = new User();
        dbUser.setId(userId);
        dbUser.setUsername(userName);

        when(principal.getName())
                .thenReturn(userName);
        when(this.userRepository.findUserByUsername(userName))
                .thenReturn(dbUser);

        assertTrue(this.pileService.isCurrentUserOwner(principal, searchedPileUserId));
    }

    @Test
    void canCurrentUserEdit_WhenPileDoesNOTExists_ShouldThrowPileNotFoundException() {
        String searchedPileId = "-1";

        when(this.pileRepository.existsPileById(searchedPileId))
                .thenReturn(false);

        assertThrows(PileNotFoundException.class,
                () -> this.pileService.canCurrentUserEdit(null, searchedPileId));
    }

    @Test
    void canCurrentUserEdit_WhenPileIsOnPartyMode_ShouldReturnTrue() {
        String searchedPileId = "1";
        Pile dbPile = new Pile();
        dbPile.setId(searchedPileId);
        dbPile.setReadOnly(false);

        when(this.pileRepository.existsPileById(searchedPileId))
                .thenReturn(true);
        when(this.pileRepository.findPileById(searchedPileId))
                .thenReturn(dbPile);

        Boolean canCurrentUserEdit = this.pileService.canCurrentUserEdit(null, searchedPileId);

        assertTrue(canCurrentUserEdit);
    }

    @Test
    void canCurrentUserEdit_WhenPileIsNotOwnedByCurrentUserAndIsReadOnly_ShouldReturnFalse() {
        String searchedPileId = "1";
        String currentUserName = "non-owner-user";
        String currentId = "non-owner-user";
        String ownerUserName = "owner-user";
        String ownerUserId = "owner-user";
        Pile dbPile = new Pile();
        dbPile.setId(searchedPileId);
        dbPile.setReadOnly(true);
        User dbOwnerUser = new User();
        dbOwnerUser.setUsername(ownerUserName);
        dbOwnerUser.setId(ownerUserId);
        dbPile.setUser(dbOwnerUser);
        User dbNonOwnerUser = new User();
        dbNonOwnerUser.setUsername(currentUserName);
        dbNonOwnerUser.setId(currentId);



        when(this.pileRepository.existsPileById(searchedPileId))
                .thenReturn(true);
        when(this.pileRepository.findPileById(searchedPileId))
                .thenReturn(dbPile);
        when(principal.getName())
                .thenReturn(currentUserName);
        when(this.userRepository.findUserByUsername(currentUserName))
                .thenReturn(dbNonOwnerUser);

        Boolean canCurrentUserEdit = this.pileService.canCurrentUserEdit(principal, searchedPileId);

        assertFalse(canCurrentUserEdit);
    }

    @Test
    void canCurrentUserEdit_WhenPileIsOwnedByCurrentUserAndIsReadOnly_ShouldReturnTrue() {
        String searchedPileId = "1";
        String currentUserName = "owner-user";
        String currentId = "owner-user";
        String ownerUserName = "owner-user";
        String ownerUserId = "owner-user";
        Pile dbPile = new Pile();
        dbPile.setId(searchedPileId);
        dbPile.setReadOnly(true);
        User dbOwnerUser = new User();
        dbOwnerUser.setUsername(ownerUserName);
        dbOwnerUser.setId(ownerUserId);
        dbPile.setUser(dbOwnerUser);
        User dbNonOwnerUser = new User();
        dbNonOwnerUser.setUsername(currentUserName);
        dbNonOwnerUser.setId(currentId);



        when(this.pileRepository.existsPileById(searchedPileId))
                .thenReturn(true);
        when(this.pileRepository.findPileById(searchedPileId))
                .thenReturn(dbPile);
        when(principal.getName())
                .thenReturn(currentUserName);
        when(this.userRepository.findUserByUsername(currentUserName))
                .thenReturn(dbNonOwnerUser);

        Boolean canCurrentUserEdit = this.pileService.canCurrentUserEdit(principal, searchedPileId);

        assertTrue(canCurrentUserEdit);
    }

    @Test
    void canCurrentUserChangeMode_WhenPileWithIdDoesNotExists_ShouldThrowPileNotFoundException() {
        String currentAccessedPileId = "-1";

        when(this.pileRepository.existsPileById(currentAccessedPileId))
                .thenReturn(false);

        assertThrows(PileNotFoundException.class,
                ()-> this.pileService.canCurrentUserChangeMode(null, currentAccessedPileId));
    }

    @Test
    void canCurrentUserChangeMode_WhenPrincipalIsNull_ShouldReturnFalse() {
        String currentAccessedPileId = "1";
        Pile currentAccessedPile = new Pile();
        currentAccessedPile.setId(currentAccessedPileId);

        when(this.pileRepository.existsPileById(currentAccessedPileId))
                .thenReturn(true);
        when(this.pileRepository.findPileById(currentAccessedPileId))
                .thenReturn(currentAccessedPile);

        Boolean canCurrentUserChangeMode = this.pileService.canCurrentUserChangeMode(null,currentAccessedPileId);
        assertFalse(canCurrentUserChangeMode);
    }

    @Test
    void canCurrentUserChangeMode_WhenCurrentUserIsOwnerOfTheAccessedPile_ShouldReturnTrue() {
        String currentAccessedPileId = "1";
        User currentUser = new User();
        String currentUserId = "owner-user-id";
        String currentUserUserName = "owner-user-username";
        currentUser.setId(currentUserId);
        currentUser.setUsername(currentUserUserName);

        Pile currentAccessedPile = new Pile();
        currentAccessedPile.setId(currentAccessedPileId);
        currentAccessedPile.setUser(currentUser);


        when(this.pileRepository.existsPileById(currentAccessedPileId))
                .thenReturn(true);
        when(this.pileRepository.findPileById(currentAccessedPileId))
                .thenReturn(currentAccessedPile);
        when(principal.getName())
                .thenReturn(currentUserUserName);
        when(this.userRepository.findUserByUsername(currentUserUserName))
                .thenReturn(currentUser);

        Boolean canCurrentUserChangeMode = this.pileService.canCurrentUserChangeMode(principal,currentAccessedPileId);
        assertTrue(canCurrentUserChangeMode);
    }

    @Test
    void canCurrentUserChangeMode_WhenCurrentUserIsNOTOwnerOfTheAccessedPile_ShouldReturnFalse() {
        String currentAccessedPileId = "1";
        User currentUser = new User();
        String currentUserId = "non-owner";
        String currentUserUserName = "non-owner";
        currentUser.setId(currentUserId);
        currentUser.setUsername(currentUserUserName);

        Pile currentAccessedPile = new Pile();
        currentAccessedPile.setId(currentAccessedPileId);
        User ownerPileUser = new User();
        ownerPileUser.setId("owner");
        ownerPileUser.setUsername("owner");
        currentAccessedPile.setUser(ownerPileUser);


        when(this.pileRepository.existsPileById(currentAccessedPileId))
                .thenReturn(true);
        when(this.pileRepository.findPileById(currentAccessedPileId))
                .thenReturn(currentAccessedPile);
        when(principal.getName())
                .thenReturn(currentUserUserName);
        when(this.userRepository.findUserByUsername(currentUserUserName))
                .thenReturn(currentUser);

        Boolean canCurrentUserChangeMode = this.pileService.canCurrentUserChangeMode(principal,currentAccessedPileId);
        assertFalse(canCurrentUserChangeMode);
    }
}