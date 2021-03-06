<content:title>
    <fmt:message key="add.app.category" />
</content:title>

<content:section cssId="appCategoryCreatePage">
    <h4><content:gettitle /></h4>
    <div class="card-panel">
        <form:form modelAttribute="appCategory">
            <tag:formErrors modelAttribute="appCategory" />
            
            <form:hidden path="project" value="${appCategory.project.id}" />

            <div class="row">
                <div class="input-field col s12">
                    <form:label path="name" cssErrorClass="error"><fmt:message key='name' /></form:label>
                    <form:input path="name" cssErrorClass="error" />
                </div>
                
                <div class="input-field col s12">
                    <form:label path="backgroundColor" cssErrorClass="error"><fmt:message key='background.color' /> (RGB)</form:label>
                    <form:input path="backgroundColor" cssErrorClass="error" placeholder="250,250,250" />
                </div>
            </div>

            <button id="submitButton" class="btn deep-purple lighten-1 waves-effect waves-light" type="submit">
                <fmt:message key="add" /> <i class="material-icons right">send</i>
            </button>
        </form:form>
    </div>
</content:section>
