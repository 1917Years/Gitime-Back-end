package capstone.gitime.domain.uploadfile.entity;

import capstone.gitime.domain.common.entity.BaseTimeEntity;
import capstone.gitime.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFile extends BaseTimeEntity {

    @Id
    @Column(name = "upload_file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uploadFileName;

    private String storeFileName;

    // 단방향 매핑으로 시작
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "upload_file_member_fk"))
    private Member member;

    @Builder(builderMethodName = "createFileEntity")
    public UploadFile(String uploadFileName, String storeFileName, Member member) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.member = member;
    }
}
