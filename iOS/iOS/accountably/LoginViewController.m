//
//  LoginViewController.m
//  accountably
//
//  Created by Steven Palomino on 1/12/15.
//  Copyright (c) 2015 Steven Palomino. All rights reserved.
//

#import "LoginViewController.h"
#import <Parse/Parse.h>
#import "ExpensesTableViewController.h"

@interface LoginViewController ()
@property (nonatomic, assign) id currentResponder;

@end

@implementation LoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [[self navigationController]setNavigationBarHidden:NO];
    self.usernameLabel.delegate = self;
    self.passwordLabel.delegate = self;
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(resignOnTap:)];
    [singleTap setNumberOfTapsRequired:1];
    [singleTap setNumberOfTouchesRequired:1];
    [self.view addGestureRecognizer:singleTap];

    // Do any additional setup after loading the view.
}

- (void)resignOnTap:(id)sender {
    [self.currentResponder resignFirstResponder];
}

- (void)textFieldDidBeginEditing:(UITextField *)textField {
    self.currentResponder = textField;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)loginButton:(UIButton *)sender {
    
    if ((![self.usernameLabel.text isEqualToString:@""]) && (![self.passwordLabel.text isEqualToString:@""])) {
        PFUser *currentUser = [PFUser user];
        currentUser.username = self.usernameLabel.text;
        currentUser.password = self.passwordLabel.text;
        
        [PFUser logInWithUsernameInBackground:currentUser.username password:currentUser.password block:^(PFUser *user, NSError *error) {
            if (user) {
                // Success
                [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"isLoggedIn"];
                [[NSUserDefaults standardUserDefaults]synchronize];
                ExpensesTableViewController *membersAreaVC = [self.storyboard instantiateViewControllerWithIdentifier:@"members"];
                UINavigationController *nav = [[UINavigationController alloc]initWithRootViewController:membersAreaVC];
                [self presentViewController:nav animated:YES completion:nil];
            } else {
                // Failure
                UIAlertView *parseError = [[UIAlertView alloc]initWithTitle:nil message:error.localizedDescription delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
                [parseError show];
            }
        }];
        
    }else{
        UIAlertView *emptyFields = [[UIAlertView alloc]initWithTitle:nil message:@"Username or Password fields cannot be empty" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
        [emptyFields show];
    }
    
}
@end
