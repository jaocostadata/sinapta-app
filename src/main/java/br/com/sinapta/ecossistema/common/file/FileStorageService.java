package br.com.sinapta.ecossistema.common.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path baseDir;
    private final AttachmentRepository attachmentRepository;

    public FileStorageService(@Value("${app.storage.base-dir}") String baseDir,
                               AttachmentRepository attachmentRepository) {
        this.baseDir = Path.of(baseDir).toAbsolutePath().normalize();
        this.attachmentRepository = attachmentRepository;
        try {
            Files.createDirectories(this.baseDir);
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível criar o diretório de armazenamento", e);
        }
    }

    public Attachment store(String ownerType, String ownerId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio");
        }
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "arquivo" : file.getOriginalFilename());
        String extension = originalFileName.contains(".") ? originalFileName.substring(originalFileName.lastIndexOf('.')) : "";
        String storedFileName = UUID.randomUUID() + extension;

        try {
            Files.copy(file.getInputStream(), baseDir.resolve(storedFileName));
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao salvar arquivo " + originalFileName, e);
        }

        Attachment attachment = new Attachment(ownerType, ownerId, originalFileName, storedFileName,
                file.getContentType(), file.getSize());
        return attachmentRepository.save(attachment);
    }

    public List<Attachment> listFor(String ownerType, String ownerId) {
        return attachmentRepository.findByOwnerTypeAndOwnerId(ownerType, ownerId);
    }

    public Resource loadAsResource(Attachment attachment) {
        try {
            Path filePath = baseDir.resolve(attachment.getStoredFileName()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new IllegalStateException("Arquivo não encontrado: " + attachment.getOriginalFileName());
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Caminho de arquivo inválido", e);
        }
    }
}
