package dev.mehdizebhi.uploads3.service;

import dev.mehdizebhi.uploads3.model.Attachment;
import dev.mehdizebhi.uploads3.model.AttachmentEntity;
import dev.mehdizebhi.uploads3.repository.AttachmentRepository;
import dev.mehdizebhi.uploads3.utils.AttachmentAssembler;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttachmentDaoService {

    private final AttachmentRepository repository;
    private final AttachmentAssembler assembler;

    public Optional<AttachmentEntity> register(Attachment attachment) {
        AttachmentEntity entity = repository.save(toEntity(attachment));
        return Optional.of(entity);
    }

    public Optional<AttachmentEntity> findByFileId(String fileId) {
        return repository.findByFileId(fileId);
    }

    public Optional<AttachmentEntity> findById(ObjectId id) {
        return repository.findById(id);
    }

    public List<Attachment> findByPageable(Pageable pageable) {
        return repository.findAll(pageable).map(assembler::toModel).stream().toList();
    }

    public void delete(ObjectId id) {
        repository.deleteById(id);
    }

    public void deleteAll(List<ObjectId> ids){
        repository.deleteAllById(ids);
    }
    private AttachmentEntity toEntity(Attachment attachment) {
        AttachmentEntity entity = AttachmentEntity.builder().build();
        BeanUtils.copyProperties(attachment, entity);
        return entity;
    }
}
