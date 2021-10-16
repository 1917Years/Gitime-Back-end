package capstone.gitime.domain.uploadfile.repository;

import capstone.gitime.domain.uploadfile.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {

    Optional<UploadFile> findByStoreFileName(String storeFileName);
}
