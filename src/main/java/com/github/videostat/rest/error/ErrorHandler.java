package com.github.videostat.rest.error;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Locale;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;

import com.github.videostat.meta.VideoMetaException;

@ControllerAdvice
public class ErrorHandler {

	private final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

	private Locale locale = LocaleContextHolder.getLocale();

	@Autowired
	private MessageSource messageSource;

	@Value("${spring.http.multipart.max-file-size:}")
	private String fileSizeLimit;

	@ExceptionHandler(VideoMetaException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO processInvalidVideoException(VideoMetaException e) {
		return new ErrorDTO(message(ErrorConstants.ERR_VIDEO_PROCESSING));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO processValidationError(MethodArgumentNotValidException ex) {
		return processValidationError(ex.getBindingResult());
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO processValidationError(BindingResult ex) {
		List<FieldError> fieldErrors = ex.getFieldErrors();
		ErrorDTO dto = new ErrorDTO(message(ErrorConstants.ERR_VALIDATION));
		for (FieldError fieldError : fieldErrors) {
			dto.add(fieldError.getField(),
					message(fieldError.getCode(), fieldError.getArguments()));
		}
		return dto;
	}

	@ExceptionHandler(MultipartException.class)
	@ResponseBody
	@ResponseStatus
	public ErrorDTO processMultipartException(MultipartException ex) {
		if (ex.contains(FileSizeLimitExceededException.class)) {
			return new ErrorDTO(message(ErrorConstants.ERR_FILE_LIMIT, fileSizeLimit));
		} else {
			return new ErrorDTO(message(ErrorConstants.ERR_REQUEST_LIMIT, fileSizeLimit));
		}
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public ErrorDTO processAccessDeniedException(AccessDeniedException e) {
		return new ErrorDTO(message(ErrorConstants.ERR_ACCESS_DENIED), e.getMessage());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public ErrorDTO processMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
		return new ErrorDTO(message(ErrorConstants.ERR_METHOD_NOT_SUPPORTED), exception.getMessage());
	}


	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDTO> processException(Exception ex) {
		if (log.isDebugEnabled()) {
			log.debug("An unexpected error occured: {}", ex.getMessage(), ex);
		} else {
			log.error("An unexpected error occured: {}", ex.getMessage());
		}
		BodyBuilder builder;
		ErrorDTO errorVM;
		ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
		if (responseStatus != null) {
			builder = ResponseEntity.status(responseStatus.value());
			errorVM = new ErrorDTO("error." + responseStatus.value().value(), responseStatus.reason());
		} else {
			builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			// for security reasons we don't tell exact message for production
			errorVM = new ErrorDTO(message(ErrorConstants.ERR_INTERNAL_SERVER_ERROR), "Internal server error");
		}
		return builder.body(errorVM);
	}

	private String message(String code, Object... args) {
		return messageSource.getMessage(code, args, code, locale);
	}

}
