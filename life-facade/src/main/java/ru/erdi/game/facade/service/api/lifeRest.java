package ru.erdi.game.facade.service.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ru.erdi.game.facade.types.Form;
import ru.erdi.game.facade.types.InfoEntity;
import ru.erdi.game.facade.types.InfoGeneration;

import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ru.erdi.game.facade.types.Entity;
import ru.erdi.game.facade.types.Error;


@Path("/")
@WebService
@Produces("application/json; charset=UTF-8")
@Consumes("application/json; charset=UTF-8")
public interface lifeRest {
	
	@POST
	@Path("seventhday")
	@ApiOperation(value = "seventhday", 
		notes = "Создание мира", 
		response = InfoEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Успешный ответ"),
							@ApiResponse(code = 400, message = "Неправильный запрос", response = Error.class) })
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public InfoEntity seventhday(@ApiParam(name="form", value = "Начальная форма заполнения", required = true) Form form);
	
	@POST
	@Path("nextday")
	@ApiOperation(value = "nextday", 
		notes = "Создание следующего дня/новое поколение", 
		response = InfoEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Успешный ответ"),
							@ApiResponse(code = 400, message = "Неправильный запрос", response = Error.class) })
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public InfoEntity nextday(@ApiParam(name="InfoGeneration", value = "Информация о поколении", required = true) InfoGeneration req);
	
}
