//
//  AddExpenseViewController.m
//  accountably
//
//  Created by Steven Palomino on 1/14/15.
//  Copyright (c) 2015 Steven Palomino. All rights reserved.
//

#import "AddExpenseViewController.h"
#import <Parse/Parse.h>
#import "Connectivity.h"

@interface AddExpenseViewController ()
@property (nonatomic, assign) id currentResponder;
@property (nonatomic, strong) NSArray *priorities;
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
    self.priorities = @[@"- Select a Priority -", @"1 - Low Priority", @"2 - Medium Priority", @"3 - High Priority"];
    self.expensePriority.delegate = self;
    
    
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

-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}

-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    return self.priorities.count;
}

-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    return [self.priorities objectAtIndex:row];
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
    Connectivity *conn = [[Connectivity alloc]init];
    if ([conn isConnected]) {
        NSLog(@"connected");
        if ((![self.expenseName.text isEqualToString:@""]) && (![self.expenseAmount.text isEqualToString:@""])) {
            
            PFUser *user = [PFUser currentUser];
            PFObject *expense = [PFObject objectWithClassName:@"Expense"];
            expense[@"expenseName"] = self.expenseName.text;
            NSNumber *expenseAmount = [[NSNumber alloc]initWithFloat:self.expenseAmount.text.floatValue];
            expense[@"expenseAmount"] = expenseAmount;
            expense[@"user"] = user;
            expense[@"expensePriority"] = [NSNumber numberWithLong:[self.expensePriority selectedRowInComponent:0]];
            [expense saveInBackground];
            [[NSNotificationCenter defaultCenter] postNotificationName:@"savedExpense"
                                                                object:nil
                                                              userInfo:nil];
            [self dismissViewControllerAnimated:YES completion:nil];
            
        }else{
            UIAlertView *empty = [[UIAlertView alloc]initWithTitle:nil message:@"Expense name or amount cannot be blank" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
            [empty show];
        }
    
    }else{
        NSLog(@"not connected");
        UIAlertView *notConn = [[UIAlertView alloc]initWithTitle:nil message:@"No network connection detected." delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
        [notConn show];
    }
    
    
}















@end
