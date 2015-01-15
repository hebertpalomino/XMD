//
//  AddExpenseViewController.h
//  accountably
//
//  Created by Steven Palomino on 1/14/15.
//  Copyright (c) 2015 Steven Palomino. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AddExpenseViewController : UIViewController<UITextFieldDelegate>
@property (weak, nonatomic) IBOutlet UITextField *expenseName;
@property (weak, nonatomic) IBOutlet UITextField *expenseAmount;
- (IBAction)tappedAddButton:(UIButton *)sender;

@end
