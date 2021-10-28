
import { createStore, applyMiddleware } from 'redux';
import createSagaMiddleware, { END } from 'redux-saga';
import logger from 'redux-logger';
// 暂时注释了
import rootReducer from '../index';

const middlewares = [];

//日志处理部分

// configuring saga middleware
// 异步操作中间件
const sagaMiddleware = createSagaMiddleware();

middlewares.push(sagaMiddleware);
// /* global __DEV__  */
// if (__DEV__) {
  middlewares.push(logger);
// }
const createStoreWithMiddleware = applyMiddleware(...middlewares)(createStore);

export default function store(initialState) {

  const store = createStoreWithMiddleware(rootReducer, initialState);
  // install saga run
  store.runSaga = sagaMiddleware.run;
  store.close = () => store.dispatch(END);

  return store;
}
