package ru.erdi.game.facade.types;

import java.util.UUID;

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
@ApiModel(value="Entity", discriminator = "Информация об объекте")
public class Entity {
	@ApiModelProperty(value = "Координата Х")
	private int x;
	@ApiModelProperty(value = "Координата У")
	private int y;
	
}