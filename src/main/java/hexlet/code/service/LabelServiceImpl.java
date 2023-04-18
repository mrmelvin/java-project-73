package hexlet.code.service;


import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;


    @Override
    public Label createLabel(LabelDto labelDto) {
        Label label = new Label();
        label.setName(labelDto.getName());
        return labelRepository.save(label);
    }

    @Override
    public Label updateLabel(Long id, LabelDto labelDto) {
        Label label = labelRepository.findById(id).get();
        merge(label, labelDto);
        return labelRepository.save(label);
    }

    private void merge(final Label label, final LabelDto labelDto) {
        final Label newLabel = fromDto(labelDto);
        label.setName(newLabel.getName());
    }

    private Label fromDto(final LabelDto labelDto) {
        return Label.builder().name(labelDto.getName()).build();
    }
}
