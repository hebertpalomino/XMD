//
//  UpdateViewController.m
//  accountably
//
//  Created by Steve Palomino on 1/27/15.
//  Copyright (c) 2015 Steven Palomino. All rights reserved.
//

#import "UpdateViewController.h"
#import <Parse/Parse.h>
#import "Connectivity.h"

@interface UpdateViewController ()
@property (nonatomic, strong) NSArray *priorities;
@property (nonatomic, assign) id currentResponder;
@property (nonatomic, retain) NSNumber *amount;

@end

@implementation UpdateViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.expenseName.delegate = self;
    self.expenseAmount.delegate = self;
    NSLog(@"NAME: %@", self.currentExpense.name);
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(resignOnTap:)];
    [singleTap setNumberOfTapsRequired:1];
    [singleTap setNumberOfTouchesRequired:1];
    [self.view addGestureRecognizer:singleTap];
    self.expensePriority.delegate = self;
    self.priorities = @[@"- Select a Priority -", @"1 - Low Priority", @"2 - Medium Priority", @"3 - High Priority"];
    
    self.expenseName.text = self.currentExpense.name;
    self.expenseAmount.text = self.currentExpense.amount.stringValue;
    [self.expensePriority selectRow:self.currentExpense.aPriority.intValue inComponent:0 animated:NO];
    

    // Do any additional setup after loading the view.
}

- (void)resignOnTap:(id)sender {
    NSLog(@"working");
    [self.currentResponder resignFirstResponder];
}

- (void)textFieldDidBeginEditing:(UITextField *)textField {
    self.currentResponder = textField;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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


- (IBAction)tappedUpdate:(UIButton *)sender {
    
    Connectivity *conn = [[Connectivity alloc]init];
    if ([conn isConnected]) {
        if ([self validateName:self.expenseName.text]) {
            
            if ([self validateAmount:self.expenseAmount.text]) {
                if ([self validatePriority:[self.expensePriority selectedRowInComponent:0]]) {

                    PFQuery *query = [PFQuery queryWithClassName:@"Expense"];
                    //[query whereKey:@"playerName" equalTo:@"Dan Stemkoski"];
                    NSLog(@"%@", self.currentExpense.objectID);
                    [query getObjectInBackgroundWithId:self.currentExpense.objectID block:^(PFObject *object, NSError *error) {
                        if (!error) {
                            //success
                            object[@"expenseName"] = self.expenseName.text;
                            NSNumber *expenseAmount = [[NSNumber alloc]initWithFloat:self.expenseAmount.text.floatValue];
                            object[@"expenseAmount"] = expenseAmount;
                            object[@"expensePriority"] = [NSNumber numberWithLong:[self.expensePriority selectedRowInComponent:0]];
                            [object saveInBackground];
                            
                            
                        }else{
                            //failure
                        }
                    }];//query block

                    [[NSNotificationCenter defaultCenter] postNotificationName:@"savedExpense"
                                                                        object:nil
                                                                      userInfo:nil];
                    [self.navigationController popViewControllerAnimated:YES];
        
                }else{
                    //priority validation
                    UIAlertView *prio = [[UIAlertView alloc]initWithTitle:nil message:@"Must choose priority to continue" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
                    [prio show];
                }
            }else{
                //amount validation
                UIAlertView *amount = [[UIAlertView alloc]initWithTitle:nil message:@"Must enter valid expense amount to continue" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
                [amount show];
            }
            
        }else{
            //name validation
            UIAlertView *name = [[UIAlertView alloc]initWithTitle:nil message:@"Must enter expense name to continue" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
            [name show];
        }
    }else{
        NSLog(@"not connected");
        UIAlertView *notConn = [[UIAlertView alloc]initWithTitle:nil message:@"No network connection detected." delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
        [notConn show];
    }
    
    
    
    
    
    
}


-(BOOL) validateName:(NSString *)name
{
    if ([name isEqualToString:@""]){
        return false;
    }else{
        return true;
    }
}

-(BOOL) validateAmount:(NSString*)amount{
    NSRange range = [amount rangeOfString:@"."];
    float floatAmount;
    int loc = range.location + 3;
    if ([amount isEqualToString:@""]){
        UIAlertView *empty = [[UIAlertView alloc]initWithTitle:nil message:@"Expense amount cannot be blank" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
        [empty show];
        return false;
    }
    if (range.length != 0) {
        if (loc != -1){
            amount = [amount substringToIndex:loc];
            floatAmount = amount.floatValue;
        }
    }else{
        floatAmount = amount.floatValue;
    }
    
    if (floatAmount > 0){
        NSLog(@"%f", floatAmount);
        self.amount = [NSNumber numberWithFloat:floatAmount];
        return true;
    }else{
        UIAlertView *empty = [[UIAlertView alloc]initWithTitle:nil message:@"Amount must be positive" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
        [empty show];
        return false;
    }
}


-(BOOL) validatePriority:(int)position{
    //pattern validation
    if (position == 1 || position == 2 || position == 3){
        return true;
    }else{
        UIAlertView *prio = [[UIAlertView alloc]initWithTitle:nil message:@"Must select priority" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
        [prio show];
        return false;
    }
}



















@end
