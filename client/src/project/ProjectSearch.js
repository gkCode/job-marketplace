import React, { Component } from "react";
import {
    getProjectById
} from "../util/APIUtils";
import LoadingIndicator from "../common/LoadingIndicator";
import { Button, Icon, notification } from "antd";
import { PROJECT_LIST_SIZE } from "../constants";
import { withRouter } from "react-router-dom";
import "./ProjectList.css";
import { Card, Input } from "antd";
import Bid from "./Bid";

class ProjectSearch extends Component {
    constructor(props){
        super(props);
        this.state = {
            query: '',
            project: '',
            isLoading: true,
            bid: ''
        };
    }

    componentWillMount() {
        if(this.props.location.query){
            this.setState({
                query: this.props.location.query,
                isLoading: true
            }, () => {
                this.loadProjectInfo(this.state)
            }); 
        }
    }

    componentWillReceiveProps(nextProps) {
        this.setState({
            query: nextProps.location.query,
            isLoading: true
        }, () => {
            this.loadProjectInfo(this.state)
        }); 
    }

    loadProjectInfo = () => {
        let promise = getProjectById(this.state.query);     

        if(!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });

        promise            
        .then(response => {
            this.setState({
                project: response,
                isLoading: false
            })
        }).catch(error => {
            this.setState({
                isLoading: false
            })
            if(error.status === 401) {
                this.props.handleLogout('/login', 'error', 'You have been logged out. Please login create project.');    
            } else {
                notification.error({
                    message: 'Unable to find project',
                    description: error.message
                });              
            }
        });  
    }

    render() {
        if(!this.state.query){
            return (
                <div className="page-not-found">
                    <div className="desc">
                        Enter Project ID
                    </div>
                </div>
            );
        }else if(this.state.isLoading) {
            return <LoadingIndicator />;
        }else{
            return (
                <div className="project-grid">
                    { 
                        <Card title="Project Details">
                            <div>  Name: {this.state.project.name}   </div>
                            <div>  Descrition: {this.state.project.description}    </div>
                            <div>  Budget: {this.state.project.budget}   </div>
                            <div>  Bid Expiration: {this.state.project.bidExpiry}  </div>  
                            <div>  Lowest Bid: {this.state.project.bid}  </div>  
                            <Bid projectId={this.state.project.id}></Bid>  
                        </Card>               
                    }    
                </div>
                );
        }   
    }
}

export default withRouter(ProjectSearch);
