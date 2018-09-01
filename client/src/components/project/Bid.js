import React, {Component} from 'react';
import {placeBid} from 'util/APIUtils';
import {withRouter} from "react-router-dom";
import {Form, Button, InputNumber, notification} from 'antd';

const FormItem = Form.Item;

class Bid extends Component {
    constructor(props) {
        super(props);
        this.state = {
            projectId: '',
            bid: '',
            validateStatus: ''
        };
    }

    componentWillMount() {
        this.setState({
            projectId: this.props.projectId,
        });
    }

    componentWillReceiveProps(nextProps) {
        this.setState({
            projectId: nextProps.projectId
        });
    }

    validateBid = (value) => {
        if (value.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: 'Please enter a valid bid ammount'
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    }

    handleBidChange = (value) => {
        this.setState({
            bid: value,
            ...this.validateBid(value)
        });
    }

    isFormInvalid = () => {
        if (this.state.validateStatus !== 'success') {
            return true;
        }
        return false;
    }

    handleSubmit = (event) => {
        event.preventDefault();
        const bidInfo = {
            projectId: this.state.projectId,
            bid: this.state.bid
        };

        placeBid(bidInfo)
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

    render() {
        return (
            <div>
                <Form onSubmit={this.handleSubmit} className="create-project-form">
                    <FormItem validateStatus={this.state.validateStatus}>
                        <InputNumber min={1} step={0.1} size="40" onChange={this.handleBidChange}/>
                    </FormItem>
                    <FormItem className="project-form-row">
                        <Button type="primary"
                                htmlType="submit"
                                size="large"
                                disabled={this.isFormInvalid()}
                                className="create-project-form-button">Place Bid</Button>
                    </FormItem>
                </Form>
            </div>
        );
    }
}

export default withRouter(Bid);