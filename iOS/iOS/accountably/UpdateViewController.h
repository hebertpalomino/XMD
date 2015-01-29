//
//  UpdateViewController.h
//  accountably
//
//  Created by Steve Palomino on 1/27/15.
//  Copyright (c) 2015 Steven Palomino. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Expense.h"

@interface UpdateViewController : UIViewController<UITextFieldDelegate, UIPickerViewDelegate, UIPickerViewDataSource>

@property (nonatomic, strong) Expense *currentExpense;
@property (weak, nonatomic) IBOutlet UITextField *expenseName;
@property (weak, nonatomic) IBOutlet UITextField *expenseAmount;
@property (weak, nonatomic) IBOutlet UIPickerView *expensePriority;
- (IBAction)tappedUpdate:(UIButton *)sender;

@end
