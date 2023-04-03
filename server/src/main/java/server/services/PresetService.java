package server.services;

import commons.Board;
import commons.Preset;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.CardRepository;
import server.database.PresetRepository;

import javax.transaction.TransactionScoped;
import javax.transaction.Transactional;
import java.util.List;
@Service
public class PresetService {
    private PresetRepository presetRepo;
    private BoardRepository boardRepo;

    private CardRepository cardRepo;

    public PresetService(PresetRepository presetRepository,
                         BoardRepository boardRepo,
                         CardRepository cardRepo){
        this.presetRepo = presetRepository;
        this.boardRepo = boardRepo;
        this.cardRepo = cardRepo;
    }

    public List<Preset> getAllPresets(){
        return this.presetRepo.findAll();
    }

    public Preset getById(int id) throws Exception{
        Preset preset = presetRepo.findById(id)
                .orElseThrow(
                        ()->new Exception("Preset with id: "+id+" does not exist")
                );
        return preset;
    }

    @Transactional
    public Preset add(Preset preset) throws Exception{
        if(!boardRepo.existsById(preset.boardId))
            throw new Exception("Board does not exist");
        if(preset.id!=null && presetRepo.existsById(preset.id))
            throw new Exception("Preset with id: " + preset.id +" already exists");
        Board board = this.boardRepo.findById(preset.boardId).get();
        board.addCardPreset(preset);
        return presetRepo.save(preset);
    }

    @Transactional
    public Preset deleteById(int id)throws Exception{
        Preset preset = presetRepo.findById(id)
                .orElseThrow(
                        ()->new Exception("Preset with id: " + id +" not found")
                );
        presetRepo.deleteById(id);
        return preset;
    }

    @Transactional
    public Preset editPresetBackgroundById(int id, String background) throws Exception{
        Preset preset = presetRepo.findById(id)
                .orElseThrow(
                        ()->new Exception("Preset with id: " + id +" not found")
                );
        preset.backgroundColor= background;
        return presetRepo.save(preset);
    }

    @Transactional
    public Preset editPresetFontById(int id, String font) throws Exception{
        Preset preset = presetRepo.findById(id)
                .orElseThrow(
                        ()->new Exception("Preset with id: " + id +" not found")
                );
        preset.font= font;
        return presetRepo.save(preset);
    }
    @TransactionScoped
    public Preset setAsDefault(int id) throws Exception{
        Preset preset = presetRepo.findById(id)
                .orElseThrow(
                        ()->new Exception("Preset with id: " + id +" not found")
                );

        Board board = boardRepo.findById(preset.boardId)
                .orElseThrow(
                        ()->new Exception("Board with id: " + preset.boardId +" not found")
                );

        board.defaultCardPreset = preset;
        boardRepo.save(board);

        return presetRepo.save(preset);

    }




}
