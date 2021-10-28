import bindActionCreators from "react-redux/lib/utils/bindActionCreators";
import * as globalActions from "../../reducers/GlobleReducers";
import {connect} from "react-redux";
import { Link,useHistory} from 'react-router-dom';
import { Button } from "element-react";

const ErrorPage403 = (props) =>{

    const history = useHistory();
    const goBack = ()=>{
        history.goBack();
    }

    return(
        <div className="error-page">
            <div className="error-code">4<span>0</span>3</div>
            <div className="error-desc">你没有权限访问该页面!!!</div>
            <div className="error-handle">
                <Link to="/">
                    <Button type="primary" size="small">返回首页</Button>
                </Link>
                <Button className="error-btn" type="primary" size="small" onClick={goBack}>返回上一页</Button>

            </div>
        </div>
    )
}


const mapStateToProps = (state,ownProps) => {
    let global =  state.global;

    return {

    };
};

const mapDispatchToProps = dispatch => {
    const action = bindActionCreators(globalActions, dispatch);
    return {
        pageAction:action,
        dispatch,
    };
};

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(ErrorPage403);
