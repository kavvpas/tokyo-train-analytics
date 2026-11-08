package com.tokyo.train.exception;

import com.tokyo.train.model.dto.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/** Controller で発生した例外を共通形式の JSON に変換する。 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgument(IllegalArgumentException e) {
        return Result.error(400, "リクエストが不正です: " + e.getMessage());
    }

    /** パラメータの検証エラー(例: limit=0)。HTTP 400 と共通形式の JSON を返す。 */
    @ExceptionHandler({HandlerMethodValidationException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBadParameter(Exception e) {
        return Result.error(400, "リクエストのパラメータが不正です");
    }

    /** 存在しないパス(ブラウザが自動で取りに来る favicon.ico など)は 404 を返し、ERROR ログにしない。 */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handleNotFound(NoResourceFoundException e) {
        return Result.error(404, "リソースが見つかりません");
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("未処理の例外", e);
        return Result.error(500, "サーバーエラーが発生しました");
    }
}
