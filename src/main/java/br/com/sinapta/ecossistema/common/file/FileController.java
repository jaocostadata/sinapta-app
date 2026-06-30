package br.com.sinapta.ecossistema.common.file;

import br.com.sinapta.ecossistema.common.exception.ResourceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Generic "anexar arquivo" endpoint shared by every module (contracts,
 * financial entries, etc). ownerType identifies the module, ownerId the
 * specific record the file belongs to.
 */
@RestController
public class FileController {

    private final FileStorageService fileStorageService;
    private final AttachmentRepository attachmentRepository;

    public FileController(FileStorageService fileStorageService, AttachmentRepository attachmentRepository) {
        this.fileStorageService = fileStorageService;
        this.attachmentRepository = attachmentRepository;
    }

    @PostMapping("/api/anexos")
    public Attachment upload(@RequestParam String ownerType,
                              @RequestParam String ownerId,
                              @RequestPart MultipartFile file) {
        return fileStorageService.store(ownerType, ownerId, file);
    }

    @GetMapping("/api/anexos")
    public List<Attachment> list(@RequestParam String ownerType, @RequestParam String ownerId) {
        return fileStorageService.listFor(ownerType, ownerId);
    }

    @GetMapping("/api/anexos/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable UUID id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anexo não encontrado: " + id));
        Resource resource = fileStorageService.loadAsResource(attachment);
        String contentType = attachment.getContentType() != null ? attachment.getContentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getOriginalFileName() + "\"")
                .body(resource);
    }
}
