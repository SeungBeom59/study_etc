package polymorphism;

import org.modelmapper.internal.bytebuddy.description.type.TypeList;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.CoroutinesUtils;
import org.springframework.core.KotlinDetector;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.RequestPath;
import org.springframework.lang.Nullable;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.*;
import org.springframework.web.context.request.async.AsyncWebRequest;
import org.springframework.web.context.request.async.WebAsyncManager;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.support.WebContentGenerator;
import org.springframework.web.util.ServletRequestPathUtils;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;

public class d {
}

protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HttpServletRequest processedRequest = request;  // 요청을 담은 변수
    HandlerExecutionChain mappedHandler = null; // 핸들러 저장할 변수
    boolean multipartRequestParsed = false; // 요청이 multipart 형식인지 여부

    WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request); // 비동기 요청 처리위한 객체

    try {
        ModelAndView mv = null;
        Exception dispatchException = null;

        try {
            processedRequest = checkMultipart(request); // multipart 형식인지 여부 체크
            multipartRequestParsed = (processedRequest != request);

            // Determine handler for the current request.
            // 요청에 매핑되는 HandlerMapping 조회
            mappedHandler = getHandler(processedRequest);
            if (mappedHandler == null) { // 핸들러 없다면,
                noHandlerFound(processedRequest, response); // 404 not_found 반환
                return;
            }

            // Determine handler adapter for the current request.
            // Handler을 수행할 수 있는 HandlerAdapter 조회
            HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

            // Process last-modified header, if supported by the handler.
            String method = request.getMethod();    // 요청 http 메소드 가져오기
            boolean isGet = HttpMethod.GET.matches(method); // get인지 여부 확인
            if (isGet || HttpMethod.HEAD.matches(method)) { // get 메소드 또는 HEAD 메소드인 경우
                // 리소스 최종 수정값 가져오기 (deprecated spring 5.3.9)
                long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
                // 클라이언트의 리소스 버전과 서버의 최신 리소스 비교, 맞다면 바로 요쳥 종료
                if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
                    return;
                }
            }
            // 등록된 인터셉터의 preHandle 수행 (컨트롤러 실행 전처리)
            // HandlerInterceptor 인터페이스 사용하여 요청 전후에 대한 처리가 가능.
            if (!mappedHandler.applyPreHandle(processedRequest, response)) {
                return;
            }

            // Actually invoke the handler.
            // HandlerAdapter를 통해 Controller 호출 (실제 로직)
            // 요청 , 응답 , 처리할 핸들러(컨트롤러) 를 인자로 핸들러 실행.
            // 메서드가 반환하는 ModelAndView (view 정보와 view에 전달할 model)
            mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

            if (asyncManager.isConcurrentHandlingStarted()) { // 비동기 요청 처리 상태 여부 반환, 시작 true
                return;
            }

            // modelAndView에서 view 이름을 설정 ,
            // 명시적으로 반환해주는 view 이름이고 없다면 url 분석하여 사용
            applyDefaultViewName(processedRequest, mv);
            // 등록된 인터셉터의 postHandle 수행 (컨트롤러 실행 후처리) , 렌더링 이전에 처리한다는 점
            mappedHandler.applyPostHandle(processedRequest, response, mv);
        }
        catch (Exception ex) {
            dispatchException = ex;
        }
        catch (Throwable err) {
            // As of 4.3, we're processing Errors thrown from handler methods as well,
            // making them available for @ExceptionHandler methods and other scenarios.
            dispatchException = new ServletException("Handler dispatch failed: " + err, err);
        }
        // dispatch 결과 처리
        processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
    }
    catch (Exception ex) {
        triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
    }
    catch (Throwable err) {
        triggerAfterCompletion(processedRequest, response, mappedHandler,
                new ServletException("Handler processing failed: " + err, err));
    }
    finally {
        // 비동기 요청 처리 진행중인 경우 진행
        if (asyncManager.isConcurrentHandlingStarted()) {
            // Instead of postHandle and afterCompletion
            if (mappedHandler != null) {
                mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
            }
        }
        else {
            // Clean up any resources used by a multipart request.
            // multipart 인 경우에 사용했던 리소스들 정리
            if (multipartRequestParsed) {
                cleanupMultipart(processedRequest);
            }
        }
    }
}

public class RequestMappingHandlerAdapter extends AbstractHandlerMethodAdapter
        implements BeanFactoryAware, InitializingBean {

    @Override
    protected ModelAndView handleInternal(HttpServletRequest request,
                                          HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {

        ModelAndView mav;   // 반환할 ModelAndView
        checkRequest(request); // 요청 검증

        // Execute invokeHandlerMethod in synchronized block if required.
        // 세션 (동시성) 관련 설정
        if (this.synchronizeOnSession) { // 세션 동기화 여부
            // session false는 새로운 세션의 생성요청임.
            HttpSession session = request.getSession(false);
            if (session != null) {
                Object mutex = WebUtils.getSessionMutex(session);
                synchronized (mutex) {
                    // mutext가 무엇인지 모르겠으나, 스레드 안전하게 컨트롤러 실행
                    mav = invokeHandlerMethod(request, response, handlerMethod);
                }
            }
        }
        else {
            // No synchronization on session demanded at all...
            // 컨트롤러 실행
            mav = invokeHandlerMethod(request, response, handlerMethod);
        }

        // 캐시 기능에 대한 설정
        if (!response.containsHeader(HEADER_CACHE_CONTROL)) { // 응답헤더에 캐시 컨트롤 헤더필드가 있는가?
            if (getSessionAttributesHandler(handlerMethod).hasSessionAttributes()) {
                // 세션 사용하는 경우 지정된 캐시 시간 설정
                applyCacheSeconds(response, this.cacheSecondsForSessionAttributeHandlers);
            }
            else {
                // 세션 사용안하면 기본적인 응답 헤더 설정
                prepareResponse(response);
            }
        }

        return mav;
    }
}

public class ServletModelAttributeMethodProcessor extends ModelAttributeMethodProcessor {}
public class ModelAttributeMethodProcessor implements HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler {}


@SuppressWarnings("deprecation")
@Nullable
protected ModelAndView invokeHandlerMethod(HttpServletRequest request,
                                           HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {

    // 비동기 요청 처리 관련 설정 , Spring 5.3 이후로 새로운 비동기 API 사용 권장이므로 위의 deprecation 달아놓음.
    WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
    AsyncWebRequest asyncWebRequest = WebAsyncUtils.createAsyncWebRequest(request, response);
    asyncWebRequest.setTimeout(this.asyncRequestTimeout);

    asyncManager.setTaskExecutor(this.taskExecutor);
    asyncManager.setAsyncWebRequest(asyncWebRequest);
    asyncManager.registerCallableInterceptors(this.callableInterceptors);
    asyncManager.registerDeferredResultInterceptors(this.deferredResultInterceptors);

    // Obtain wrapped response to enforce lifecycle rule from Servlet spec, section 2.3.3.4
    response = asyncWebRequest.getNativeResponse(HttpServletResponse.class);

    // 요청과 응답을 하나로 감싸주는 ServletWebRequest 생성?
    ServletWebRequest webRequest = (asyncWebRequest instanceof ServletWebRequest ?
            (ServletWebRequest) asyncWebRequest : new ServletWebRequest(request, response));

    WebDataBinderFactory binderFactory = getDataBinderFactory(handlerMethod); // data 바인딩 처리
    ModelFactory modelFactory = getModelFactory(handlerMethod, binderFactory); // 모델 생성

    // 실행가능한 핸들러 메서드 생성?
    ServletInvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
    // 요청 파라미터를 변환하는 리졸버 설정
    if (this.argumentResolvers != null) {
        invocableMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
    }
    // 핸들러(컨트롤러)의 반환값을 처리하는 핸들러 설정
    if (this.returnValueHandlers != null) {
        invocableMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
    }
    // 추가적인 설정
    invocableMethod.setDataBinderFactory(binderFactory);
    invocableMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);

    // 컨트롤러의 처리결과인 ModelAndView를 담는 컨테이너 생성
    ModelAndViewContainer mavContainer = new ModelAndViewContainer();
    mavContainer.addAllAttributes(RequestContextUtils.getInputFlashMap(request));
    modelFactory.initModel(webRequest, mavContainer, invocableMethod);
    mavContainer.setIgnoreDefaultModelOnRedirect(this.ignoreDefaultModelOnRedirect);

    // 핸들러 메서드의 비동기 작업 처리 진행 여부 확인
    if (asyncManager.hasConcurrentResult()) {
        Object result = asyncManager.getConcurrentResult();
        Object[] resultContext = asyncManager.getConcurrentResultContext();
        Assert.state(resultContext != null && resultContext.length > 0, "Missing result context");
        mavContainer = (ModelAndViewContainer) resultContext[0];
        asyncManager.clearConcurrentResult();
        LogFormatUtils.traceDebug(logger, traceOn -> {
            String formatted = LogFormatUtils.formatValue(result, !traceOn);
            return "Resume with async result [" + formatted + "]";
        });
        invocableMethod = invocableMethod.wrapConcurrentResult(result);
    }
    // 실제 컨트롤러(핸들러) 실행
    invocableMethod.invokeAndHandle(webRequest, mavContainer);
    if (asyncManager.isConcurrentHandlingStarted()) { // 또 비동기 관련, 비동기 처리중인 경우 null 반환
        return null;
    }

    return getModelAndView(mavContainer, modelFactory, webRequest); // ModelAndView로 만들어 반환
}

public interface HandlerMethodArgumentResolver {

    // 파라미터의 어노테이션을 찾아 지원가능 여부를 반환한다.
    boolean supportsParameter(MethodParameter parameter);

    // 클라이언트의 요청 값을 파라미터로 변환하여 반환해주는 과정을 갖는다.
    // 컨트롤러 호출 이전에 바인딩 과정이므로 컨트롤러보다 먼저 클라이언트의 값을 살펴보고
    // 특정 어노테이션이 붙은 파라미터 타입들에 대하여 공통된 로직을 넣을 수도 있다.
    @Nullable
    Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                           NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception;
}

public class ServletInvocableHandlerMethod extends InvocableHandlerMethod {

    public void invokeAndHandle(ServletWebRequest webRequest, ModelAndViewContainer mavContainer,
                                Object... providedArgs) throws Exception {
        // 리졸버 변환, 컨트롤러의 생성과 실행 ,
        Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);
    ...
    }
}

public class InvocableHandlerMethod extends HandlerMethod {

    @Nullable
    public Object invokeForRequest(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer,
                                   Object... providedArgs) throws Exception {
        // 리졸버 변환 처리
        Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
        if (logger.isTraceEnabled()) {
            logger.trace("Arguments: " + Arrays.toString(args));
        }
        // 컨트롤러 메소드 실행
        return doInvoke(args);
    }
}

public class InvocableHandlerMethod extends HandlerMethod {

    @Nullable
    protected Object doInvoke(Object... args) throws Exception {
        Method method = getBridgedMethod();
        try {
            // 코틀린 관련이므로 무시.
            if (KotlinDetector.isSuspendingFunction(method)) {
                return CoroutinesUtils.invokeSuspendingFunction(method, getBean(), args);
            }
            // 메소드를 실행한다.
            // 인자로 getBean()으로 실행할 Bean 객체를 인스턴스로 생성,
            // args는 리졸버에서 처리한 파라미터 바인딩 값들
            return method.invoke(getBean(), args);
        }
    ...
    }
}

public class InvocableHandlerMethod extends HandlerMethod {

    protected Object[] getMethodArgumentValues(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer,
                                               Object... providedArgs) throws Exception {
        // 파라미터 비었는지?
        MethodParameter[] parameters = getMethodParameters();
        if (ObjectUtils.isEmpty(parameters)) {
            return EMPTY_ARGS;
        }
        // 파라미터 생성 및 바인딩
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            MethodParameter parameter = parameters[i];
            parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
            args[i] = findProvidedArgument(parameter, providedArgs);
            if (args[i] != null) {
                continue;
            }
            // 해석 변환 가능하니?
            if (!this.resolvers.supportsParameter(parameter)) {
                throw new IllegalStateException(formatArgumentError(parameter, "No suitable resolver"));
            }
            try {
                // 변환 동작
                args[i] = this.resolvers.resolveArgument(parameter, mavContainer, request, this.dataBinderFactory);
            }
            ....
        }
    }
}

public final class BridgeMethodResolver {

    public static Method findBridgedMethod(Method bridgeMethod) {
        // 브릿지 메소드가 맞긴 합니까?
        if (!bridgeMethod.isBridge()) {
            return bridgeMethod; // 인자로 들어왔던 오리지널 메소드를 반환
        }
        // 캐시에 등록된 브릿지 메소드가 있다면 가져올 것.
        Method bridgedMethod = cache.get(bridgeMethod);
        // 아직도 없나?
        if (bridgedMethod == null) {
             // 브릿지 메소드를 찾는 로직
            }
            if (bridgedMethod == null) {
                // A bridge method was passed in but we couldn't find the bridged method.
                // Let's proceed with the passed-in method and hope for the best...
                // 못찾으면 그냥 인자로 받은 오리지널 메소드를 넣는 모습, 행운을 빈다고...
                bridgedMethod = bridgeMethod;
            }
            // 캐시에 넣어놔라. 나중에 빠르게 찾아서 쓸 수 있도록.
            cache.put(bridgeMethod, bridgedMethod);
        }
        // 브릿지메소드 반환.
        return bridgedMethod;
    }
}

public class Node<T> {

    public T data;

    public Node(T data) { this.data = data; }

    public void setData(T data) {
        System.out.println("Node.setData");
        this.data = data;
    }
}

public class MyNode extends Node<Integer> {
    public MyNode(Integer data) { super(data); }

    public void setData(Integer data) {
        System.out.println("MyNode.setData");
        super.setData(data);
    }
}

public class Node {

    public Object data;

    public Node(Object data) { this.data = data; }

    public void setData(Object data) {
        System.out.println("Node.setData");
        this.data = data;
    }
}

public class MyNode extends Node {

    public MyNode(Integer data) { super(data); }

    public void setData(Integer data) {
        System.out.println("MyNode.setData");
        super.setData(data);
    }
}

MyNode mn = new MyNode(5);
Node n = mn;            // A raw type - compiler throws an unchecked warning
n.setData("Hello");     // Causes a ClassCastException to be thrown.
Integer x = mn.data;

MyNode mn = new MyNode(5);
Node n = mn;            // A raw type - compiler throws an unchecked warning
// Note: This statement could instead be the following:
//     Node n = (Node)mn;
// However, the compiler doesn't generate a cast because
// it isn't required.
n.setData("Hello");     // Causes a ClassCastException to be thrown.
Integer x = (Integer)mn.data;

public void invokeAndHandle(ServletWebRequest webRequest, ModelAndViewContainer mavContainer,
                            Object... providedArgs) throws Exception {

    Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);
    // 아래 진행
    setResponseStatus(webRequest);

    if (returnValue == null) {
        if (isRequestNotModified(webRequest) || getResponseStatus() != null || mavContainer.isRequestHandled()) {
            disableContentCachingIfNecessary(webRequest);
            mavContainer.setRequestHandled(true);
            return;
        }
    }
    else if (StringUtils.hasText(getResponseStatusReason())) {
        mavContainer.setRequestHandled(true);
        return;
    }

    mavContainer.setRequestHandled(false);
    Assert.state(this.returnValueHandlers != null, "No return value handlers");
    try {
        this.returnValueHandlers.handleReturnValue(
                returnValue, getReturnValueType(returnValue), mavContainer, webRequest);
    }
    catch (Exception ex) {
        if (logger.isTraceEnabled()) {
            logger.trace(formatErrorForReturnValue(returnValue), ex);
        }
        throw ex;
    }
}


@SuppressWarnings("deprecation")
@Nullable
protected ModelAndView invokeHandlerMethod(HttpServletRequest request,
                                           HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {

    ...

    invocableMethod.invokeAndHandle(webRequest, mavContainer); // 여기로 반환
    if (asyncManager.isConcurrentHandlingStarted()) {
        return null;
    }

    return getModelAndView(mavContainer, modelFactory, webRequest);
}

private ModelAndView getModelAndView(ModelAndViewContainer mavContainer,
                                     ModelFactory modelFactory, NativeWebRequest webRequest) throws Exception {

    modelFactory.updateModel(webRequest, mavContainer);
    if (mavContainer.isRequestHandled()) {
        return null;
    }
    ModelMap model = mavContainer.getModel();
    ModelAndView mav = new ModelAndView(mavContainer.getViewName(), model, mavContainer.getStatus());
    if (!mavContainer.isViewReference()) {
        mav.setView((View) mavContainer.getView());
    }
    if (model instanceof RedirectAttributes redirectAttributes) {
        Map<String, ?> flashAttributes = redirectAttributes.getFlashAttributes();
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request != null) {
            RequestContextUtils.getOutputFlashMap(request).putAll(flashAttributes);
        }
    }
    return mav;
}

@Override
protected ModelAndView handleInternal(HttpServletRequest request,
                                      HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {

    ...

    if (this.synchronizeOnSession) {
        ...
    }
    else {
        // No synchronization on session demanded at all...
        mav = invokeHandlerMethod(request, response, handlerMethod);
    }

    if (!response.containsHeader(HEADER_CACHE_CONTROL)) {
        if (getSessionAttributesHandler(handlerMethod).hasSessionAttributes()) {
            applyCacheSeconds(response, this.cacheSecondsForSessionAttributeHandlers);
        }
        else {
            prepareResponse(response);
        }
    }

    return mav;
}
// 모델앤 뷰 반환
@Override
@Nullable
public final ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {

    return handleInternal(request, response, (HandlerMethod) handler);
}

디스패처서블릿
@SuppressWarnings("deprecation")
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ...

            // Actually invoke the handler.
            mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

            if (asyncManager.isConcurrentHandlingStarted()) {
                return;
            }

            applyDefaultViewName(processedRequest, mv);
            mappedHandler.applyPostHandle(processedRequest, response, mv);
        }
        catch (Exception ex) {
            dispatchException = ex;
        }
        catch (Throwable err) {
            // As of 4.3, we're processing Errors thrown from handler methods as well,
            // making them available for @ExceptionHandler methods and other scenarios.
            dispatchException = new ServletException("Handler dispatch failed: " + err, err);
        }
        processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
    }
    catch (Exception ex) {
        triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
    }
    catch (Throwable err) {
        triggerAfterCompletion(processedRequest, response, mappedHandler,
                new ServletException("Handler processing failed: " + err, err));
    }
    finally {
        if (asyncManager.isConcurrentHandlingStarted()) {
            // Instead of postHandle and afterCompletion
            if (mappedHandler != null) {
                mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
            }
        }
        else {
            // Clean up any resources used by a multipart request.
            if (multipartRequestParsed) {
                cleanupMultipart(processedRequest);
            }
        }
    }
}

@Override
protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logRequest(request);

    ...

    try {
        doDispatch(request, response);
    }
    finally {
        if (!WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted()) {
            // Restore the original attribute snapshot, in case of an include.
            if (attributesSnapshot != null) {
                restoreAttributesAfterInclude(request, attributesSnapshot);
            }
        }
        if (this.parseRequestPath) {
            ServletRequestPathUtils.setParsedRequestPath(previousRequestPath, request);
        }
    }
}

protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    ...

    try {
        doService(request, response);
    }
    catch (ServletException | IOException ex) {
        ...
    }
    catch (Throwable ex) {
        ...
    }
    // finally 동작
    finally {
        resetContextHolders(request, previousLocaleContext, previousAttributes);
        if (requestAttributes != null) {
            requestAttributes.requestCompleted();
        }
        logResult(request, response, failureCause, asyncManager);
        publishRequestHandledEvent(request, response, startTime, failureCause);
    }
}