import React, {Component} from 'react';
import {createProject} from '../../util/APIUtils';
import './Signup.css';
import './NewProject.css'
import {
    NAME_MIN_LENGTH, NAME_MAX_LENGTH,
    DESCRIPTION_MAX_LENGTH
} from 'constants/AppConstants';

import {Form, Input, Button, notification, DatePicker} from 'antd';

const FormItem = Form.Item;
const { TextArea } = Input;

class NewProject extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: {
                value: ''
            },
            description: {
                value: ''
            },
            budget: {
                value: ''
            },
            bidExpiry: {
                value: ''
            }
        }
    }

    handleInputChange = (event, validate) => {
        const target = event.target;
        console.log(target);
        const inputName = target.name;
        const inputValue = target.value;

        this.setState({
            [inputName]: {
                value: inputValue,
                ...validate(inputValue)
            }
        });
    }

    handleSubmit = (event) => {
        event.preventDefault();

        const projectData = {
            name: this.state.name.value,
            description: this.state.description.value,
            budget: this.state.budget.value,
            bidExpiry: this.state.bidExpiry.value
        };

        createProject(projectData)
            .then(response => {
                this.props.history.push("/");
            }).catch(error => {
            if (error.status === 401) {
                this.props.handleLogout('/login', 'error', 'You have been logged out. Please login create project.');
            } else {
                notification.error({
                    message: 'Job Marketplace',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            }
        });
    }

    isFormInvalid = () => {
        return !(this.state.name.validateStatus === 'success' &&
            this.state.description.validateStatus === 'success' &&
            this.state.budget.validateStatus === 'success' &&
            this.state.bidExpiry.validateStatus === 'success'
        );
    }

    validateName = (name) => {
        if (name.length < NAME_MIN_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Name is too short (Minimum ${NAME_MIN_LENGTH} characters needed.)`
            }
        } else if (name.length > NAME_MAX_LENGTH) {
            return {
                validationStatus: 'error',
                errorMsg: `Name is too long (Maximum ${NAME_MAX_LENGTH} characters allowed.)`
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        }
    }

    validateBudget = (budget) => {
        if (!budget) {
            return {
                validateStatus: 'error',
                errorMsg: 'Budget may not be empty'
            }
        }

        const BUDGET_REGEX = RegExp('[^@ ]+@[^@ ]+\\.[^@ ]+');
        if (!BUDGET_REGEX.test(budget) && false) {
            return {
                validateStatus: 'error',
                errorMsg: 'Budget not valid'
            }
        }

        return {
            validateStatus: null,
            errorMsg: null
        }
    }

    validateDescription = (description) => {
        if (description.length > DESCRIPTION_MAX_LENGTH) {
            return {
                validationStatus: 'error',
                errorMsg: `description is too long (Maximum ${DESCRIPTION_MAX_LENGTH} characters allowed.)`
            }
        } else {
            return {
                validateStatus: null,
                errorMsg: null
            }
        }
    }

    validateBidExpiry = (bidExpiry) => {
       if (bidExpiry === null) {
            return {
                validationStatus: 'error',
                errorMsg: `Enter a valid bid expiration date`
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        }
    }

    getDisabledDates = (current) => {
        return current && current.valueOf() < Date.now();
    }

    render() {
        return (
            <div className="new-project-container">
                <h1 className="page-title">Enter Project Details</h1>
                <div className="new-project-content">
                    <Form onSubmit={this.handleSubmit} className="signup-form">
                        <FormItem
                            label="Name"
                            validateStatus={this.state.name.validateStatus}
                            help={this.state.name.errorMsg}>
                            <Input
                                size="default"
                                name="name"
                                autoComplete="off"
                                placeholder="Project Name"
                                value={this.state.name.value}
                                onChange={(event) => this.handleInputChange(event, this.validateName)}/>
                        </FormItem>
                        <FormItem label="Description"
                                  hasFeedback
                                  validateStatus={this.state.description.validateStatus}
                                  help={this.state.description.errorMsg}>
                            <TextArea
                                size="default"
                                name="description"
                                autoComplete="off"
                                placeholder="Project Description"
                                value={this.state.description.value}
                                onBlur={this.validateDescriptionAvailability}
                                onChange={(event) => this.handleInputChange(event, this.validateDescription)}/>
                        </FormItem>
                        <FormItem
                            label="Budget"
                            hasFeedback
                            validateStatus={this.state.budget.validateStatus}
                            help={this.state.budget.errorMsg}>
                            <Input
                                size="default"
                                name="budget"
                                autoComplete="off"
                                placeholder="Project Cost Budget"
                                value={this.state.budget.value}
                                onBlur={this.validateBudgetAvailability}
                                onChange={(event) => this.handleInputChange(event, this.validateBudget)}/>
                        </FormItem>
                        <FormItem
                            label="Bid Expiration"
                            validateStatus={this.state.bidExpiry.validateStatus}
                            help={this.state.bidExpiry.errorMsg}>
                            <DatePicker
                                size="default"
                                placeholder="Project Bid Expiry Date"
                                disabledDate={this.getDisabledDates}
                                value={this.state.bidExpiry.value}
                                onChange={(event) => this.handleInputChange(event, this.validateBidExpiry)}/>
                        </FormItem>
                        <FormItem>
                            <Button type="primary"
                                    htmlType="submit"
                                    size="large"
                                    className="signup-form-button"
                                    disabled={this.isFormInvalid()}>Add Project</Button>
                        </FormItem>
                    </Form>
                </div>
            </div>
        );
    }

}

export default NewProject;