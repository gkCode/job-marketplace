import React, { Component } from 'react';
import { createProject } from '../util/APIUtils';
import './NewProject.css';  
import { Form, Input, Button, Icon, Select, Col, notification, DatePicker } from 'antd';
import Moment from 'react-moment';
import moment from 'moment';
const Option = Select.Option;
const FormItem = Form.Item;
const { TextArea } = Input

class NewProject extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            description: '',
            budget: '',
            bidExpiry: ''
        };
    }

    handleSubmit = (event) => {
        event.preventDefault();
        const projectData = {
            name: this.state.name,
            description: this.state.description,
            budget: this.state.budget,
            bidExpiry: this.state.bidExpiry
        };

        createProject(projectData)
        .then(response => {
            this.props.history.push("/");
        }).catch(error => {
            if(error.status === 401) {
                this.props.handleLogout('/login', 'error', 'You have been logged out. Please login create project.');    
            } else {
                notification.error({
                    message: 'Job Marketplace',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });              
            }
        });
    }

    validateName = (name) => {
        if(name.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: 'Please enter valid name!'
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    }

    handleNameChange = (event) => {
        const value = event.target.value;
        this.setState({
                name: value,
                ...this.validateName(value)
        });
    }

    handleDescriptionChange = (event) => {
        const value = event.target.value;
        this.setState({
                description: value,
                ...this.validateName(value)
        });
    }

    handleBudgetChange = (event) => {
        const value = event.target.value;
        this.setState({
                budget: value
                // ...this.validateBid(value)
        });
    }

    handleBidExpiry = (value) => {
        this.setState({
                bidExpiry: value.toDate()
                // ...this.validateBid(value)
        });
    }

    isFormInvalid = () => {
        if(this.state.validateStatus !== 'success') {
            return true;
        }
        return false;
    }

    render() {
        return (
            <div className="new-project-container">
                <h1 className="page-title">Enter Project Details</h1>
                <div className="new-project-content">
                    <Form onSubmit={this.handleSubmit} className="create-project-form">
                        <FormItem validateStatus={this.state.validateStatus}
                            className="project-form-row">
                            <Input.Group>
                                <Input placeholder="Name"
                                    style = {{ fontSize: '16px', marginTop:10, marginBottom:10 }} 
                                    name = "Name"
                                    onChange = {this.handleNameChange} />
                                <Input placeholder="Description"
                                    style = {{ fontSize: '16px', marginTop:10, marginBottom:10 }} 
                                    name = "Description"
                                    onChange = {this.handleDescriptionChange} />
                                <Input placeholder="Budget"
                                    style = {{ fontSize: '16px', marginTop:10, marginBottom:10 }} 
                                    name = "Budget"
                                    onChange = {this.handleBudgetChange} />
                                <DatePicker placeholder="Budget Expiration Date"
                                    onChange = {this.handleBidExpiry} 
                                    style = {{ fontSize: '16px', width: '230px', marginTop:10, marginBottom:10 }} />
                            </Input.Group>
                        </FormItem>
                        <FormItem className="project-form-row">
                            <Button type="primary" 
                                htmlType="submit" 
                                size="large" 
                                disabled={this.isFormInvalid()}
                                className="create-project-form-button">Create Project</Button>
                        </FormItem>
                    </Form>
                </div>    
            </div>
        );
    }
}

export default NewProject;