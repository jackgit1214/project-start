import {BrowserRouter as Router, Redirect, Route, Switch} from 'react-router-dom';
import Header from "../components/Header"
import SliderBar from "../components/SliderBar";
import {useDispatch, useSelector,useStore} from 'react-redux'
import UserPage from "../views/user/UserPage";
import routes from "../router";

const switchRoutes = (
	<Switch>
		{routes.map((prop, key) => {
			// if (prop.layout === '/admin') {
			return (
				<Route
					path={prop.path}
					component={prop.component}
					key={key}
				/>
			);
			// }
			// return null;
		})}
	</Switch>
);


const IndexHome = () => {
	const collapseStatus = useSelector((state => state.global.collapseStatus))
	const loginInfo = useSelector((state=>state.global.loginInfo))
	return loginInfo?(
		<div>
			<Router>
				<Header/>
				<SliderBar/>
				<div className={collapseStatus ? "content-box " : "content-box content-collapse"}>
					<div className="content">
						{switchRoutes}
					</div>
				</div>
			</Router>
		</div>
	):(
		<Redirect to="/login"/>
	)
}

export default IndexHome;