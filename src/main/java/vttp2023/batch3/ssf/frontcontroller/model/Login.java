package vttp2023.batch3.ssf.frontcontroller.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Login {

    @NotBlank(message = "Fill in your username")
    @Size(min = 2, message = "Your username must be at least 2 characters in length")
    private String username;

    @NotBlank(message = "Fill in your password")
    @Size(min = 2, message = "Your password must be at least 2 characters in length")
    private String password;

    private String captcha;
    private double correctAnswer;
    private double userAnswer;

    private boolean authenticated = false;

    private boolean locked = false;

    // constructor
    public Login() {
    }

    // getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }
    public void setCaptcha() {

        double num1 = (int)(Math.random() * 50 + 1);
        double num2 = (int)(Math.random() * 50 + 1);
        double answer = 0;
        
        String operator = Integer.toString((int) (Math.random() * 4 + 1));
        switch (operator) {
            case "1":
                answer = num1 + num2;
                operator = "+";
                break;
            case "2":
                answer = num1 - num2;
                operator = "-";
                break;
            case "3":
                answer = num1 / num2;
                operator = "/";
                break;
            case "4":
                answer = num1 * num2;
                operator = "*";
                break;
        }

        setCorrectAnswer(answer);
        this.captcha = num1 + " " + operator + " " + num2;
    }

    public double getCorrectAnswer() {
        return correctAnswer;
    }
    public void setCorrectAnswer(double correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public double getUserAnswer() {
        return userAnswer;
    }
    public void setUserAnswer(double userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
    public void setAuthenticated() {
        this.authenticated = true;
    }

    public boolean isLocked() {
        return locked;
    }
    public void setLocked() {
        this.locked = true;
    }

    // toString method
    @Override
    public String toString() {
        return "Login [username=" + username + ", password=" + password + ", captcha=" + captcha + ", answer=" + correctAnswer
                + ", authenticated=" + authenticated + ", locked=" + locked + "]";
    }

}
