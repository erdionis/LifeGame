package ru.erdi.game.facade.utils;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


public class GsonUtil {	
	private static DateLibrary DATE_LIBRARY=DateLibrary.joda;
	private static Gson gson;
	private static Gson gsonExpose;

	public static synchronized Gson getInstance() {
		if (gson == null) {
			gson = getInstanceGB(false).create();
		}
		return gson;
	}

	public static Gson getExposeInstance() {
		if (gsonExpose == null) {
			gsonExpose = getInstanceGB(true).create();
		}
		return gsonExpose;
	}

	private static Gson getInstance(boolean onlyExpose) {
		if (!onlyExpose) {
			if (gson == null) {
				gson = getInstanceGB(false).create();
			}
			return gson;
		} else {
			if (gsonExpose == null) {
				gsonExpose = getInstanceGB(true).create();
			}
			return gsonExpose;
		}
	}
	
	private static GsonBuilder getInstanceGB(boolean onlyExpose) {
		GsonBuilder builder=new GsonBuilder();
		
		switch (DATE_LIBRARY){
		case java8:
			builder=getGsonBuilderInstanceJava8(builder,onlyExpose);
		case joda:
			builder=getGsonBuilderInstanceJoda(builder, onlyExpose);
		case calendar:
			builder=getGsonBuilderInstanceCalendar(builder, onlyExpose);
		default:
			builder=getGsonBuilderInstanceJava8(builder, onlyExpose);
		}
		
		// byte[] -> string -> byte[]
		builder.registerTypeAdapter(byte[].class, (JsonSerializer<byte[]>) (src, typeOfSrc, context) -> new JsonPrimitive(Base64.getEncoder().encodeToString(src)));
		builder.registerTypeAdapter(byte[].class, (JsonDeserializer<byte[]>) (json, typeOfT, context) -> Base64.getDecoder().decode(json.getAsString()));

		return builder;
	}
	
	private static java.util.Calendar parseDate(String lexicalXSDDate) {
        return DatatypeConverter.parseDate(lexicalXSDDate.trim());
    }
	
	private static String printDate(java.util.Calendar dateTime) {
		return DatatypeConverter.printDateTime(dateTime);
	}

	private static GsonBuilder getGsonBuilderInstanceJava8(GsonBuilder gsonBuilder, boolean onlyExpose) {
		if (onlyExpose) {
			gsonBuilder.excludeFieldsWithoutExposeAnnotation();
		}
		gsonBuilder.registerTypeAdapter(Date.class,
				new JsonDeserializer<Date>() {
					@Override
					public Date deserialize(JsonElement json, Type type,
							JsonDeserializationContext arg2)
							throws JsonParseException {
							return new Date(parseDate(json.getAsString()).getTimeInMillis());
					}

				});
		gsonBuilder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
			@Override
			public JsonElement serialize(Date date, Type typeOfSrc,
					JsonSerializationContext context) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				return date == null ? null : new JsonPrimitive(printDate(calendar));
				
			}
		});
		return gsonBuilder;
	}
	


	private static GsonBuilder getGsonBuilderInstanceJoda(GsonBuilder gsonBuilder, boolean onlyExpose) {
		if (onlyExpose) {
			gsonBuilder.excludeFieldsWithoutExposeAnnotation();
		}
		gsonBuilder.registerTypeAdapter(DateTime.class,
				new JsonDeserializer<DateTime>() {
					@Override
					public DateTime deserialize(JsonElement json, Type type,
							JsonDeserializationContext arg2)
							throws JsonParseException {
						return new DateTime(parseDate(json.getAsString()).getTimeInMillis());
					}

				});
		gsonBuilder.registerTypeAdapter(DateTime.class, new JsonSerializer<DateTime>() {
			@Override
			public JsonElement serialize(DateTime datetime, Type typeOfSrc,
					JsonSerializationContext context) {
				
				return new JsonPrimitive(ISODateTimeFormat
	                    .dateTimeNoMillis()
	                    .print(datetime));
			}
		});
		return gsonBuilder;
	}	
	
	private static GsonBuilder getGsonBuilderInstanceCalendar(GsonBuilder gsonBuilder, boolean onlyExpose) {
		if (onlyExpose) {
			gsonBuilder.excludeFieldsWithoutExposeAnnotation();
		}
		gsonBuilder.registerTypeAdapter(Calendar.class,
				new JsonDeserializer<Calendar>() {
					@Override
					public Calendar deserialize(JsonElement json, Type type,
							JsonDeserializationContext arg2)
							throws JsonParseException {
							return parseDate(json.getAsString());
					}

				});
		gsonBuilder.registerTypeAdapter(Calendar.class, new JsonSerializer<Calendar>() {
			@Override
			public JsonElement serialize(Calendar calendar, Type typeOfSrc,
					JsonSerializationContext context) {
						return calendar == null ? null : new JsonPrimitive(printDate(calendar));
			}
		});
		return gsonBuilder;
	}
	
	public static <T> T fromJson(String json, Class<T> classOfT,
			boolean onlyExpose) {
		try {
			return getInstance(onlyExpose).fromJson(json, classOfT);
		} catch (Exception ex) {
			return null;
		}
	}
}