package hexlet.code.app.service;

import hexlet.code.app.dto.StatusDto;
import hexlet.code.app.model.Status;
import hexlet.code.app.repository.StatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class StatusServiceImpl implements StatusService {

	private final StatusRepository statusRepository;


	@Override
	public Status createNewStatus(StatusDto statusDto) {
		final Status status = new Status();
		status.setName(statusDto.getName());
		return statusRepository.save(status);
	}

	@Override
	public Status updateStatus(Long statusId, StatusDto statusDto) {
		final Status status = statusRepository.findById(statusId).get();
		status.setName(statusDto.getName());
		return statusRepository.save(status);
	}
}
