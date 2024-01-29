import React, { Component } from 'react';
import { BrowserRouter, Route, Switch, Redirect } from 'react-router-dom';
import LoginForm from './components/LoginForm';
import RegisterForm from './components/RegisterForm';
//import Home from './components/Home';
import Dashboard from './components/Dashboard';
import PaperDetailedView from './components/PaperDetailedView';
//import UploadAbstract from './components/UploadAbstract';
class App extends Component {
  render() {
    return (
      <BrowserRouter>
        <Switch>
          {/* <Redirect exact from="/" to="/login" /> */}
          <Route exact path="/login" component={LoginForm} />
          <Route exact path="/register" component={RegisterForm} />
          <Route exact path="/dashboard" component={Dashboard} />
          <Route exact path="/dashboard/:paperId" component={PaperDetailedView} />
          
        </Switch>
      </BrowserRouter>
    );
  }
}

export default App;