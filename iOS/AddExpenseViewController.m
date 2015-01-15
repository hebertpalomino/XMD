//
//  AddExpenseViewController.m
//  accountably
//
//  Created by Steven Palomino on 1/14/15.
//  Copyright (c) 2015 Steven Palomino. All rights reserved.
//

#import "AddExpenseViewController.h"
#import <Parse/Parse.h>

@interface AddExpenseViewController ()
@property (nonatomic, assign) id currentResponder;

@end

@implementation AddExpenseViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.expenseName.delegate = self;
    self.expenseAmount.delegate = self;
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(resignOnTap:)];
    [singleTap setNumberOfTapsRequired:1];
    [singleTap setNumberOfTouchesRequired:1];
    [self.view addGestureRecognizer:singleTap];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)resignOnTap:(id)sender {
    [self.currentResponder resignFirstResponder];
}

- (void)textFieldDidBeginEditing:(UITextField *)textField {
    self.currentResponder = textField;
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)tappedAddButton:(UIButton *)sender {
    if ((![self.expenseName.text isEqualToString:@""]) && (![self.expenseAmount.text isEqualToString:@""])) {
        
        PFUser *user = [PFUser currentUser];
        PFObject *expense = [PFObject objectWithClassName:@"expense"];
        expense[@"expenseName"] = self.expenseName.text;
        expense[@"expenseAmount"] = self.expenseAmount.text;
        expense[@"user"] = user;
        [expense saveInBackground];
        [[NSNotificationCenter defaultCenter] postNotificationName:@"savedExpense"
                                                            object:nil
                                                          userInfo:nil];
        [self dismissViewControllerAnimated:YES completion:nil];
        
    }else{
        UIAlertView *empty = [[UIAlertView alloc]initWithTitle:nil message:@"Expense name or amount cannot be blank" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
        [empty show];
    }
}






@end
