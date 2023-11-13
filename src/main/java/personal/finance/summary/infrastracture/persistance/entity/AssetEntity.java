package personal.finance.summary.infrastracture.persistance.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author akazmierczak
 * @create 17.06.2022
 */
@Entity
@Builder(buildMethodName = "buildAsset")
@AllArgsConstructor
@Getter
@ToString()
public class AssetEntity {

    public AssetEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal moneyValue;
    private String name;
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<ItemEntity> itemEntities;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        AssetEntity assetEntity = (AssetEntity) o;
        return id != null && Objects.equals(id, assetEntity.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
