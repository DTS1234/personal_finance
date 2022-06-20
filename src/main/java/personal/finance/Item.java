package personal.finance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author akazmierczak
 * @create 19.06.2022
 */
@Entity
@Builder
@Data
@AllArgsConstructor
public class Item {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assetId")
    @JsonIgnore
    private Asset asset;

    public Item() {

    }

}
