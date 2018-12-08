package ru.erdi.game.facade.types;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value="InfoGeneration", discriminator = "Информация о поколении")
public class InfoGeneration {
	@ApiModelProperty(value = "Идентификатор мира, присвоенный при создании мира")
	private String key;
	@ApiModelProperty(value = "Номер поколения")
	private Long age;
}